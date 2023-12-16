package org.hf.boot.springboot.proxy.config;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p> 自定义配置加载器 </p >
 * 需要在src/main/resources/META-INF/路径下配置spring.factories
 * 请求代理器实现 - 1
 * 加载application-proxy-conf.yml 配置项，初始化配置类ProxyServletProperties（记录路由配置信息）
 * @author HUFEI
 * @date 2023-06-05
 **/
@Slf4j
public class CustomEnvironmentPostProcessor implements EnvironmentPostProcessor {

    /**
     * 配置文件-默认加载位置
     */
    private static final String[] DEFAULT_CONF_PATH = new String[]{"classpath:/proxy/application-*.properties", "classpath:/proxy/application-*.yml"};

    /**
     * 配置文件路径-环境变量设置
     */
    private static final String PROFILES_CONF_PATH = "spring.profiles.conf.path";

    /**
     * 英文逗号隔开
     */
    private static final String EN_COMMA = ",";

    private final Map<String, PropertySourceLoader> propertySourceLoaderMap;

    private final ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    /**
     * 初始化加载文件来源信息
     */
    public CustomEnvironmentPostProcessor() {
        super();
        Map<String, PropertySourceLoader> propertySourceLoaderMap = new HashMap<>();
        List<PropertySourceLoader> propertySourceLoaders = SpringFactoriesLoader.loadFactories(PropertySourceLoader.class, getClass().getClassLoader());
        for (PropertySourceLoader propertySourceLoader : propertySourceLoaders) {
            for (String fileExtension : propertySourceLoader.getFileExtensions()) {
                propertySourceLoaderMap.put(fileExtension, propertySourceLoader);
            }
        }
        this.propertySourceLoaderMap = propertySourceLoaderMap;
    }

    /**
     * 解析指定文件信息
     * @param environment 配置变量信息
     * @param application 应用信息
     */
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        List<String> locationList = Arrays.asList(DEFAULT_CONF_PATH);
        String property = environment.getProperty(PROFILES_CONF_PATH);
        if (StringUtils.isNotBlank(property)) {
            String[] profileConfPaths = StringUtils.split(property, EN_COMMA);
            CollectionUtils.addAll(locationList, profileConfPaths);
        }
        for (String location : locationList) {
            try {
                String fileSuffix = FileUtil.getSuffix(location);
                PropertySourceLoader loader = propertySourceLoaderMap.get(fileSuffix);
                if (Objects.nonNull(loader)) {
                    Resource[] resources = this.resourcePatternResolver.getResources(location);
                    registerPropertySource(environment, loader, resources);
                } else {
                    log.error("CustomEnvironmentPostProcessor.postProcessEnvironment 配置文件{}查询不到PropertySourceLoader", location);
                }
            } catch (IOException e) {
                log.warn("CustomEnvironmentPostProcessor.postProcessEnvironment 加载配置文件{}失败", location, e);
            }
        }
    }

    /**
     * 将解析的变量信息加载到容器中
     * @param environment   配置变量信息
     * @param loader        加载器
     * @param resources     文件源信息
     * @throws IOException 异常
     */
    protected void registerPropertySource(ConfigurableEnvironment environment, PropertySourceLoader loader, Resource[] resources) throws IOException {
        for (Resource resource : resources) {
            List<PropertySource<?>> propertySources = loader.load(resource.getFilename(), resource);
            if (CollectionUtils.isNotEmpty(propertySources)) {
                propertySources.forEach(environment.getPropertySources()::addLast);
            }
        }
    }
}

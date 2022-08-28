package org.hf.common.publi.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hf.common.publi.constants.CommonConstant;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.Objects;
import java.util.Properties;

import static org.hf.common.publi.constants.CommonConstant.AFTER_BIG_BRACKETS;
import static org.hf.common.publi.constants.CommonConstant.BEFORE_BIG_BRACKETS;
import static org.hf.common.publi.constants.CommonConstant.DOLLAR_SIGN;
import static org.hf.common.publi.constants.CommonConstant.EMPTY_STR;

/**
 * <p> 获取properties配置文件信息工具类 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2021/10/10 13:22
 */
@Slf4j
public class PropertiesUtil {

    private Properties properties;
    private static PropertiesUtil propertiesUtil;

    /**
     * 私有化无参构造
     */
    private PropertiesUtil() {
        load(null);
    };

    /**
     * 初始化对象传入配置文件路径
     * @param resourcesPaths 配置文件相对路径
     */
    public PropertiesUtil(String...resourcesPaths) {
        load(resourcesPaths);
    }

    /**
     * 加载配置文件
     * @param resourcesPaths 配置文件相对路径
     */
    private void load(String[] resourcesPaths) {
        properties = new Properties();
        // PathMatchingResourcePatternResolver 对象可以根据通配符的方式加载文件资源
        PathMatchingResourcePatternResolver pathResolver = null;
        InputStream is = null;
        try {
            if (Objects.isNull(resourcesPaths) || resourcesPaths.length < 1) {
                resourcesPaths = new String[]{"application.properties"};
            }
            Resource resource;
            // 如果配置文件配置了中文,需要转码
            for (String resourcesPath : resourcesPaths) {
                // 根据文件名称获取绝对路径加载配置文件
//                is = new FileInputStream(this.getAbsolutePath(resourcesPath));
//                properties.load(is);
                // 根据文件路径加载配置文件
                if (Objects.isNull(pathResolver)) {
                    pathResolver = new PathMatchingResourcePatternResolver();
                }
                resource = pathResolver.getResource(resourcesPath);
                properties.load(resource.getInputStream());
                printAllProperty(properties);
            }
        } catch (FileNotFoundException e) {
            log.error("配置文件不存在", e);
        } catch (IOException e) {
            log.error("IO异常", e);
        }finally {
            IoUtil.close(is);
        }
    }

    /**
     * 打印解析出的配置文件中的属性及值
     * @param properties 配置项对象
     */
    private void printAllProperty(Properties properties) {
        Enumeration<?> enumeration = properties.propertyNames();
        while (enumeration.hasMoreElements()) {
            String key = String.valueOf(enumeration.nextElement());
            String value = properties.getProperty(key);
            log.info("配置文件属性名:{},值:{}", key, value);
        }
    }

    /**
     * 根据文件名称获取文件的绝对路径
     * @param fileName 文件名称
     * @return 返回文件绝对路径
     */
    private String getAbsolutePath(String fileName) {
        if (!FileUtil.isAbsolutePath(fileName)) {
            try {
                return Objects.requireNonNull(PropertiesUtil.class.getClassLoader().getResource(fileName)).toURI().getPath();
            } catch (URISyntaxException e) {
                log.error("获取配置文件绝对路径异常", e);
            }
        }
        return fileName;
    }

    /**
     * 获取当前类所获取到的所有属性的对象
     * @return properties
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     * 根据属性名获取值
     * @param key 属性名称
     * @return 返回属性值
     */
    public String getPropertiesValue(String key) {
        if (StringUtils.startsWith(key, DOLLAR_SIGN + BEFORE_BIG_BRACKETS)
                || StringUtils.endsWith(key, AFTER_BIG_BRACKETS)) {
            key = StringUtils.replace(key, DOLLAR_SIGN + BEFORE_BIG_BRACKETS, EMPTY_STR);
            key = StringUtils.replace(key, AFTER_BIG_BRACKETS, EMPTY_STR);
        }
        return String.valueOf(properties.get(key));
    }

    /**
     * 初始化当前对象
     * @return 返回当前对象实例
     */
    public static PropertiesUtil init() {
        if (null == propertiesUtil) {
            propertiesUtil = new PropertiesUtil();
        }
        return propertiesUtil;
    }

}

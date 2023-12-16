package org.hf.boot.springboot.enumerate.mvc;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hf.boot.springboot.enumerate.CommonEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 枚举注册实现, 将枚举都缓存到map中
 * 自定义枚举转换及查询处理 - 4
 */
@Slf4j
@Component
public class CommonEnumRegistry {
    private static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";
    private static final String BASE__ENUM_CLASS_NAME = CommonEnum.class.getName();

    /**
     * 枚举实例集合和类名称的映射
     */
    @Getter
    private final Map<String, List<CommonEnum>> nameDict = Maps.newLinkedHashMap();

    /**
     * 枚举实例集合和类的映射
     */
    @Getter
    private final Map<Class<?>, List<CommonEnum>> classDict = Maps.newLinkedHashMap();

    /**
     * 扫描指定路径下的类
     */
    @Value("${baseEnum.basePackage:org.hf.boot.springboot.enumerate}")
    private String basePackage;

    @Autowired
    private ResourceLoader resourceLoader;

    @PostConstruct
    public void initDict(){
        if (StringUtils.isEmpty(basePackage)){
            return;
        }
        // 1.通过Spring的ResourcePatternResolver根据配置的基础包路径(basePackage属性)对classpath进行扫描
        ResourcePatternResolver resourcePatternResolver = ResourcePatternUtils.getResourcePatternResolver(this.resourceLoader);
        MetadataReaderFactory metadataReaderFactory = new SimpleMetadataReaderFactory();
        try {
            String pkg = toPackage(this.basePackage);
            // 对 basePackage 包进行扫描
            String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                    pkg + DEFAULT_RESOURCE_PATTERN;
            // 2.获取扫描的结果Resource数组
            Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
            for (Resource resource : resources) {
                if (resource.isReadable()) {
                    try {
                        // 3. 通过MetadataReader读取Resource信息, 并解析为ClassMetadata
                        MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                        ClassMetadata classMetadata = metadataReader.getClassMetadata();
                        String[] interfaceNames = classMetadata.getInterfaceNames();
                        // 4. 获取实现 BASE_ENUM_CLASS_NAME 接口的类
                        if (Arrays.asList(interfaceNames).contains(BASE__ENUM_CLASS_NAME)){
                            String className = classMetadata.getClassName();
                            // 通过类名获取类信息
                            Class<?> clazz = Class.forName(className);
                            // 判断类是否是指定类的或是其实现
                            if (clazz.isEnum() && CommonEnum.class.isAssignableFrom(clazz)){
                                // 获取枚举实例
                                Object[] enumConstants = clazz.getEnumConstants();
                                List<CommonEnum> commonEnums = Arrays.stream(enumConstants)
                                        .filter(e -> e instanceof CommonEnum)
                                        .map(e ->(CommonEnum)e)
                                        .collect(Collectors.toList());
                                // 这里不用反射的原因: 反射会触发类的自动加载,将会对不需要进行加载的类加载, 增加metaspace的压力
                                String key = convertKeyFromClassName(clazz.getSimpleName());
                                // 5. 将指定接口的实现类缓存到集合中
                                this.nameDict.put(key, commonEnums);
                                this.classDict.put(clazz, commonEnums);
                            }
                        }
                    } catch (Throwable ex) {
                        log.error("Error in resolution enumeration", ex);
                    }
                }
            }
        } catch (IOException e) {
            log.error("Error in scanning package", e);
        }
    }

    /**
     * 转换包路径分隔符
     */
    private String toPackage(String basePackage) {
        String result = basePackage.replace(".", "/");
        return result + "/";
    }

    /**
     * 将类名首字母转换成小写
     */
    private String convertKeyFromClassName(String className){
        return Character.toLowerCase(className.charAt(0)) + className.substring(1);
    }
}
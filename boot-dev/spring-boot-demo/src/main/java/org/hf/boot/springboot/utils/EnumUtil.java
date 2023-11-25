package org.hf.boot.springboot.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hf.boot.springboot.annotations.CustomEnum;
import org.hf.boot.springboot.service.BaseEnum;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 枚举类工具类
 * 自定义实现枚举值获取 - 3
 */
@Slf4j
public class EnumUtil {

    private EnumUtil() {
    }

    /**
     * 校验enum
     *
     * @param enumClass 枚举类
     * @param enumName  枚举名称
     * @param <E>       出入参类型
     * @return boolean true-存在, false-不存在
     */
    public static <E extends Enum<E>> boolean isValidEnum(Class<E> enumClass, Object enumName) {
        return getEnum(enumClass, enumName) != null;
    }

    /**
     * 获取枚举
     *
     * @param enumClass 枚举类
     * @param enumName  枚举名称
     * @param <E>       出入参类型
     * @return 枚举
     */
    public static <E extends Enum<E>> E getEnum(Class<E> enumClass, Object enumName) {
        if (enumName == null) {
            return null;
        } else {
            try {
                // 判断是否是指定基础枚举接口子类
                if (BaseEnum.class.isAssignableFrom(enumClass)) {
                    // 获取枚举项集合
                    List<E> enumList = getEnumList(enumClass);
                    // 遍历获取具有指定枚举项自定义code的枚举
                    for (E e : enumList) {
                        if (((BaseEnum<?, ?>) e).getCode().equals(enumName)) {
                            return e;
                        }
                    }
                }
                // 获取具有指定枚举项名称的枚举
                return Enum.valueOf(enumClass, (String) enumName);
            } catch (IllegalArgumentException var3) {
                return null;
            }
        }
    }

    /**
     * 获取enum列表
     *
     * @param enumClass 枚举类
     * @param <E>       出入参类型
     * @return 枚举集合
     */
    public static <E extends Enum<E>> List<E> getEnumList(Class<E> enumClass) {
        return new ArrayList<>(Arrays.asList(enumClass.getEnumConstants()));
    }

    /**
     * 获取指定路径下的枚举类
     *
     * @param urlPattern 指定路径
     * @return 枚举类集合
     */
    public static Map<String, Class<?>> getEnumMap(String urlPattern) {
        if (StringUtils.isBlank(urlPattern)) {
            return new HashMap<>();
        }
        Map<String, Class<?>> handlerMap = new HashMap<>();
        try {
            // spring工具类, 获取指定路径下的全部类
            ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resourcePatternResolver.getResources(urlPattern);
            // MetadataReader的工厂类
            MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
            for (Resource resource : resources) {
                // 用于读取类信息
                MetadataReader reader = readerFactory.getMetadataReader(resource);
                // 扫描到的class
                String className = reader.getClassMetadata().getClassName();
                Class<?> clazz = Class.forName(className);
                // 判断是否有指定主解
                CustomEnum anno = clazz.getAnnotation(CustomEnum.class);
                if (anno != null) {
                    //将注解中的类型值作为key，对应的类作为 value
                    handlerMap.put(anno.value(), clazz);
                }
            }
        } catch (Exception e) {
            log.error("获取自定路径下={}, 的枚举类异常", urlPattern, e);
        }
        return handlerMap;
    }
}
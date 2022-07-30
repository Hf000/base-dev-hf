package org.hf.common.config.enumerate;

import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * <p> java枚举类过滤器 </p>
 * 自定义枚举注解JavaEnum实现 - 5
 * @author hufei
 * @version 1.0.0
 * @date 2021/10/12 22:07
 */
public class JavaEnumTypeFilter implements TypeFilter {

    private static String JAVA_ENUM_NAME = JavaEnum.class.getName();
    private static String ENUM_CLASS_NAME = Enum.class.getName();
    private static Set<String> NAME_VALUE_ENUM_CLASS = new HashSet<>();

    static {
        NAME_VALUE_ENUM_CLASS.add(NameValueEnum.class.getName());
        NAME_VALUE_ENUM_CLASS.add(NameStringValueEnum.class.getName());
        NAME_VALUE_ENUM_CLASS.add(NameIntegerValueEnum.class.getName());
    }

    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        // 不要使用类型比较, 此时类未被加载
        // 是否是Enum类
        ClassMetadata classMetadata = metadataReader.getClassMetadata();
        if (!ENUM_CLASS_NAME.equals(classMetadata.getSuperClassName())) {
            return false;
        }
        // 是否有javaEnum元注解
        if (!metadataReader.getAnnotationMetadata().hasAnnotation(JAVA_ENUM_NAME)) {
            return false;
        }
        // 循环带注解@JavaEnum的枚举类
        for (String interfaceName : classMetadata.getInterfaceNames()) {
            // 如果实现了指定的接口则返回
            if(NAME_VALUE_ENUM_CLASS.contains(interfaceName)) {
                return true;
            }
        }
        return false;
    }
}

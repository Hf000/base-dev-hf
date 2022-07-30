package org.hf.common.config.enumerate;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.StringUtils;

/**
 * <p> 添加<code>@JsonFormat   @com.fasterxml.jackson.annotation.JsonFormat</code>
 * 可以解决Controller返回json串, 将枚举类作为一个对象来处理 </p>
 * 自定义枚举注解JavaEnum实现 - 3
 * @author hufei
 * @version 1.0.0
 * @date 2021/10/12 21:16
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public interface NameValueEnum<V> {

    /**
     * 枚举项值/主键
     * @return 枚举值
     */
    V getValue();

    /**
     * 枚举项默认名称
     * @return 枚举名称
     */
    String getName();

    /**
     * 是否是根枚举
     * @return 根枚举标识
     */
    default String getParent() {
        return null;
    }

    /**
     * 默认实现, 请不要重写, 优先从枚举配置表取名称
     * @return 返回名称
     */
    default String getDisplayName() {
        return this.getName();
    }

    /**
     * 是否可配置, 如果为是,则表示枚举项是基于本枚举类的可选项来动态配置的 (需要从数据库读取)
     * @param enumClass  当前枚举Class
     * @param <T>        枚举类型
     * @return 是否可配置标识
     */
    static <T extends Enum<T>> boolean isConfigurableEnum(Class<? extends NameValueEnum<?>> enumClass) {
        JavaEnum javaEnum = enumClass.getAnnotation(JavaEnum.class);
        if (null == javaEnum) {
            return false;
        }
        return javaEnum.configurable();
    }

    /**
     * 获取枚举分组码code
     * @param enumClass  当前枚举Class
     * @param <T>        枚举类型
     * @return 枚举分组码code
     */
    static <T extends Enum<T>> String getEnumGroup(Class<T> enumClass) {
        JavaEnum javaEnum = enumClass.getAnnotation(JavaEnum.class);
        if (null == javaEnum) {
            return null;
        }
        String group = javaEnum.group();
        if (StringUtils.isBlank(group)) {
            group = javaEnum.value();
        }
        return group;
    }

    /**
     * 根据枚举值查找枚举项
     * @param enumClass 当前枚举类Class
     * @param value     枚举项值
     * @param <T>       枚举类型
     * @return 返回枚举项
     */
    static <T extends Enum<T>> T getEnum(Class<T> enumClass, Object value) {
        if (null == value) {
            return null;
        }
        // 得到这个枚举类下的所有枚举项
        T[] enums = getEnums(enumClass);
        if (null == enums) {
            return null;
        }
        for (T t : enums) {
            if (t instanceof NameValueEnum) {
                if(((NameValueEnum<?>) t).getValue().equals(value)) {
                    return t;
                }
            } else {
                if (t.name().equals(value)) {
                    return t;
                }
            }
        }
        return null;
    }

    /**
     * 根据枚举名称查找枚举项
     * @param enumClass 当前枚举类Class
     * @param name     枚举项名称
     * @param <T>       枚举类型
     * @return 返回枚举项
     */
    static <T extends Enum<T>> T getEnumByName(Class<T> enumClass, Object name) {
        if (null == name) {
            return null;
        }
        // 得到这个枚举类下的所有枚举项
        T[] enums = getEnums(enumClass);
        if (null == enums) {
            return null;
        }
        for (T t : enums) {
            if (t instanceof NameValueEnum) {
                if(((NameValueEnum<?>) t).getDisplayName().equals(name) || ((NameValueEnum<?>) t).getName().equals(name)) {
                    return t;
                }
            } else {
                if (t.name().equals(name)) {
                    return t;
                }
            }
        }
        return null;
    }

    /**
     * 根据枚举项值获取枚举项名称
     * @param enumClass 当前枚举类Class
     * @param value  枚举项值
     * @param <T> 枚举类型
     * @return 枚举项名称
     */
    static <T extends Enum<T>> String getEnumName(Class<T> enumClass, Object value) {
        T t = getEnum(enumClass, value);
        if (t instanceof NameValueEnum) {
            return ((NameValueEnum<?>)t).getDisplayName();
        }
        return null;
    }

    /**
     * 获取当前枚举类的所有枚举项
     * @param enumClass  当前枚举类Class
     * @param <T>        枚举类型
     * @return            所有枚举项
     */
    static <T extends Enum<T>> T[] getEnums(Class<T> enumClass) {
        if (null == enumClass) {
            return null;
        }
        return enumClass.getEnumConstants();
    }

}

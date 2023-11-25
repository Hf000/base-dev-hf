package org.hf.boot.springboot.service;

/**
 * 枚举基类接口
 * 自定义实现枚举值获取 - 2
 */
public interface BaseEnum<K, V> {

    K getCode();

    V getValue();

    /**
     * 根据值查找枚举项
     * @param code 枚举项code
     * @return 如果没有找到，返回null
     */
    static <T extends Enum<T>> T getEnum(Class<T> enumClass, Object code) {
        if (code == null) {
            return null;
        }
        // 得到这个枚举类下的所有枚举项
        T[] enums = enumClass.getEnumConstants();
        if (enums == null) {
            return null;
        }
        for (T t : enums) {
            if (t instanceof BaseEnum) {
                if (((BaseEnum<?, ?>) t).getCode().equals(code)) {
                    return t;
                }
            } else {
                if (t.name().equals(code)) {
                    return t;
                }
            }
        }
        return null;
    }
}
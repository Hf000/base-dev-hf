package org.hf.boot.springboot.enumerate;

/**
 * 枚举接口基类, 规范枚举必须的属性
 * 自定义枚举转换及查询处理 - 3
 */
public interface CommonEnum extends CodeBasedEnum, SelfDescribedEnum {

    default boolean match(String value) {
        if (value == null) {
            return false;
        }
        return value.equals(String.valueOf(getCode())) || value.equals(getName());
    }
}

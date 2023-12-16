package org.hf.boot.springboot.enumerate;

/**
 * 枚举值接口基类, 规范枚举必须的属性
 * 自定义枚举转换及查询处理 - 2
 */
public interface SelfDescribedEnum {

    default String getName() {
        return name();
    }

    String name();

    String getDescription();
}
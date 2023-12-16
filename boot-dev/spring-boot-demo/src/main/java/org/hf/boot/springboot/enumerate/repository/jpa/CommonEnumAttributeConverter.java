package org.hf.boot.springboot.enumerate.repository.jpa;

import org.hf.boot.springboot.enumerate.CommonEnum;

import javax.persistence.AttributeConverter;
import java.util.Arrays;
import java.util.List;

/**
 * spring data jpa自定义枚举转换通用处理器
 * 自定义枚举转换及查询处理 - 9
 */
public abstract class CommonEnumAttributeConverter<E extends Enum<E> & CommonEnum> implements AttributeConverter<E, Integer> {

    private final List<E> commonEnums;

    public CommonEnumAttributeConverter(E[] commonEnums){
        this(Arrays.asList(commonEnums));
    }

    public CommonEnumAttributeConverter(List<E> commonEnums) {
        this.commonEnums = commonEnums;
    }

    @Override
    public Integer convertToDatabaseColumn(E e) {
        return e.getCode();
    }

    /**
     * 实现字段类型转换
     */
    @Override
    public E convertToEntityAttribute(Integer code) {
        return commonEnums.stream()
                .filter(commonEnum -> commonEnum.match(String.valueOf(code)))
                .findFirst()
                .orElse(null);
    }
}
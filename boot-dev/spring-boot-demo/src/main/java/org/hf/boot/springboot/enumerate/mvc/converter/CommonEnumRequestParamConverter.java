package org.hf.boot.springboot.enumerate.mvc.converter;

import org.hf.boot.springboot.enumerate.CommonEnum;
import org.hf.boot.springboot.enumerate.mvc.CommonEnumRegistry;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * SpringMVC参数转换扩展 - 1: 请求路径上的参数转换 例如@RequestParam或@PathVariable方式传参
 * 自定义枚举转换及查询处理 - 6
 * 基于ConditionalGenericConverter进行扩展
 */
@Order(1)
@Component
public class CommonEnumRequestParamConverter implements ConditionalGenericConverter {

    @Autowired
    private CommonEnumRegistry enumRegistry;

    @Override
    public boolean matches(@NotNull TypeDescriptor sourceType, TypeDescriptor targetType) {
        // 匹配目标类型
        Class<?> type = targetType.getType();
        return enumRegistry.getClassDict().containsKey(type);
    }

    /**
     * 用于返回能够转换的源类型和目标类型的一个组合集合
     */
    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return enumRegistry.getClassDict().keySet().stream()
                .map(clazz -> new ConvertiblePair(String.class, clazz))
                .collect(Collectors.toSet());
    }

    /**
     * 类型转换
     */
    @Override
    public Object convert(Object source, @NotNull TypeDescriptor sourceType, TypeDescriptor targetType) {
        // 根据目标类型获取所有的枚举值, 并根据name或者code进行枚举值转换
        String value = (String) source;
        List<CommonEnum> commonEnums = this.enumRegistry.getClassDict().get(targetType.getType());
        return commonEnums.stream()
                .filter(commonEnum -> commonEnum.match(value))
                .findFirst()
                .orElse(null);
    }
}
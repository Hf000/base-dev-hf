package org.hf.boot.springboot.dynamic.statement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Stream;

/**
 * 数据字段类型枚举
 */
@AllArgsConstructor
@Getter
public enum MappingMethodEnum {

    POST("POST","post请求"),
    GET("GET","get请求"),
    ;

    private final String code;

    private final String desc;

    public static MappingMethodEnum matchByCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        return Stream.of(MappingMethodEnum.values()).filter(item -> item.getCode().equals(code)).findFirst().orElse(null);
    }
}
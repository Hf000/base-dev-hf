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
public enum DataFieldTypeEnum {

    STRING("string","字符型"),
    NUMBER("number","数值型"),
    BOOLEAN("boolean","布尔型"),
    DATE("date","日期类型"),
    ARRAY("array","数组类型")
    ;

    private final String code;

    private final String desc;

    public static DataFieldTypeEnum matchByCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        return Stream.of(DataFieldTypeEnum.values()).filter(item -> item.getCode().equals(code)).findFirst().orElse(null);
    }
}
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
public enum SelectTypeEnum {

    SELECT_ONE("SELECT_ONE","查询单行"),
    SELECT_LIST("SELECT_LIST","查询列表"),
    SELECT_PAGE("SELECT_PAGE","查询分页列表"),
    ;

    private final String code;

    private final String desc;

    public static SelectTypeEnum matchByCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        return Stream.of(SelectTypeEnum.values()).filter(item -> item.getCode().equals(code)).findFirst().orElse(null);
    }
}
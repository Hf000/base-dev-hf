package org.hf.boot.springboot.dynamic.statement;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 结果字段
 */
@Data
public class ColumnDTO {

    @ApiModelProperty("文本名称")
    private String name;

    @ApiModelProperty("参数类型：参考DataFieldTypeEnum")
    private String fieldType;

    @ApiModelProperty("字段名称")
    private String column;

    @ApiModelProperty("描述")
    private String description;
}
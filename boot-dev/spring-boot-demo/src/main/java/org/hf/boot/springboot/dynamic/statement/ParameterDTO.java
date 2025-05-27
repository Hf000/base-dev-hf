package org.hf.boot.springboot.dynamic.statement;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ParameterDTO {

    @ApiModelProperty("参数名")
    private String name;

    @ApiModelProperty("参数的位置，可能的值有：query、header、path、body。目前仅实现body")
    private String in = "body";

    @ApiModelProperty("是否必传 0：否  1：是")
    private Boolean required;

    /**
     * {@link DataFieldTypeEnum}
     */
    @ApiModelProperty("参数类型：参考DataFieldTypeEnum")
    private String fieldType;

    @ApiModelProperty("如果type是array，则必填。用于描述array里面的参数类型")
    private String arrayFieldType;

    @ApiModelProperty("参数描述")
    private String description;
}
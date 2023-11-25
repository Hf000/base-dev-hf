package org.hf.boot.springboot.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BaseEnumResp<K, V> {

    @ApiModelProperty("键")
    private K code;

    @ApiModelProperty("值")
    private V value;
}

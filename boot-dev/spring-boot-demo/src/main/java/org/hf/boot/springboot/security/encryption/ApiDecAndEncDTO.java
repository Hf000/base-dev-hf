package org.hf.boot.springboot.security.encryption;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 接口加密请求入参
 */
@Data
public class ApiDecAndEncDTO {

    @ApiModelProperty("接口注解内容")
    private ApiDecAndEnc apiDecAndEnc;

    @ApiModelProperty("请求入参数据")
    private ApiDecAndEncReq encReq;

    @ApiModelProperty("加密后的响应数据")
    private ApiDecAndEncResp encResp;

}
package org.hf.boot.springboot.security.encryption;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 接口加密出参
 */
@Data
public class ApiDecAndEncResp {

    @ApiModelProperty("加密后的密钥")
    private String encryptedKey;

    @ApiModelProperty("加密的业务数据")
    private String encryptedData;
}

package org.hf.boot.springboot.security.encryption;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hf.boot.springboot.error.BusinessException;

import javax.validation.constraints.NotBlank;

/**
 * 接口加密请求入参
 */
@Data
public class ApiDecAndEncReq {

    @ApiModelProperty("请求方来源标识")
    @NotBlank(message = "请求方来源标识不能为空")
    private String appId;

    @ApiModelProperty("加密后的密钥")
    @NotBlank(message = "加密后的密钥不能为空")
    private String encryptedKey;

    @ApiModelProperty("加密的业务数据")
    @NotBlank(message = "加密的业务数据不能为空")
    private String encryptedData;

    public void checkParam() {
        if (StringUtils.isBlank(appId)) {
            throw new BusinessException("请求方来源标识不能为空");
        }
        if (StringUtils.isBlank(encryptedKey)) {
            throw new BusinessException("加密后的密钥不能为空");
        }
        if (StringUtils.isBlank(encryptedData)) {
            throw new BusinessException("加密的业务数据不能为空");
        }
    }
}
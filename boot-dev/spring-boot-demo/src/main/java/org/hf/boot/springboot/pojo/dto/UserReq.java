package org.hf.boot.springboot.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p> 用户信息查询入参实体 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2022/8/12 22:38
 */
@ApiModel(value = "用户信息查询入参")
@Data
public class UserReq {

    @ApiModelProperty(name = "用户id")
    private Long id;

    @ApiModelProperty(name = "用户名称", required = true)
    private String userName;

}

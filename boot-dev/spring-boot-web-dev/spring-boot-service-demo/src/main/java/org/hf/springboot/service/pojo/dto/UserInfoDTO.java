package org.hf.springboot.service.pojo.dto;

import lombok.Data;

/**
 * <p> 用户信息DTO </p >
 *
 * @author hufei
 * @date 2022-09-05
 **/
@Data
public class UserInfoDTO {

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 用户name
     */
    private String username;

}
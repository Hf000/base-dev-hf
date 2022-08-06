package org.hf.springboot.service.pojo.bo;

import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author hf
 * @since 2021-11-02 17:18
 */
@Data
public class UserBo {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 姓名
     */
    private String name;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 邮箱
     */
    private String email;


}

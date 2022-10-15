package org.hf.springcloud.api.provider.pojo.dto;

import lombok.Data;

import java.util.Date;

/**
 * <p> 用户信息响应dto </p>
 * @author hufei
 * @date 2022/10/15 15:05
*/
@Data
public class UserDto {
    /**
     * id
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
     * 性别，1男性，2女性
     */
    private Integer sex;
    /**
     * 出生日期
     */
    private Date birthday;
    /**
     * 创建时间
     */
    private Date created;
    /**
     * 更新时间
     */
    private Date updated;
    /**
     * 备注
     */
    private String note;
}

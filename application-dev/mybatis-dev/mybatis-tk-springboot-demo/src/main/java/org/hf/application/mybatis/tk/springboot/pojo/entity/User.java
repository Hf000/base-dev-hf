package org.hf.application.mybatis.tk.springboot.pojo.entity;

import javax.persistence.*;
import lombok.Data;

@Data
@Table(name = "tb_user")
public class User {
    /**
     * 主键ID
     */
    @Id
    private Long id;

    /**
     * 用户名
     */
    private String user_name;

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
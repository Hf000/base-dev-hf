package org.hf.boot.springboot.pojo.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 表名：user_info
 * 表注释：用户信息表
 */
@Getter
@Setter
@ToString
@Table(name = "user_info")
public class UserInfo extends BaseEntity {
    /**
     * 自增主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SELECT LAST_INSERT_ID()")
    private Integer id;

    /**
     * 用户账号
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 姓名
     */
    private String name;

    /**
     * 年龄
     */
    private Byte age;

    /**
     * 性别 M:男,F:女,X(默认值):未知
     */
    private String sex;

    /**
     * 出生日期
     */
    private Date birthday;
}
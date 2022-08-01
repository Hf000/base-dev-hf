package org.hf.application.mybatis.tk.springboot.pojo.entity;

import java.util.Date;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hf.common.publi.pojo.entity.BaseEntity;

/**
 * 表名：user_info
*/
@Getter
@Setter
@ToString
@Table(name = "user_info")
public class UserInfo extends BaseEntity {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SELECT LAST_INSERT_ID()")
    private Integer id;

    /**
     * 用户名
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 密码
     */
    private String password;

    private String email;

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
    private Byte sex;

    /**
     * 出生日期
     */
    private Date birthday;

    /**
     * 备注
     */
    private String note;
}
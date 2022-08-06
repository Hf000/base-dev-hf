package org.hf.boot.springboot.pojo.entity;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Author:hufei
 * @CreateTime:2020-09-09
 * @Description:用户表映射实体类
 */
@Data        //在编译阶段会自动根据注解生成对应的方法，包括get/set/hashCode/toString/equals等方法；
//@Setter      //自动生成set方法
//@Getter      //自动生成get方法
@Table(name = "tb_user")            //将实体映射到数据库中对应的表
public class User {
    // 主键id
    @Id                                 //设置主键id
    @KeySql(useGeneratedKeys = true)    //主键回填，在数据库插入完数据后将自增主键id的值保存到插入的实体对象中
    private Long id;
    // 用户名
    @Column(name = "user_name")     //如果数据字段和实体对应字段一致或者实体字段符合驼峰规则（user_name --> userName）的这个注解@Column可以省略不写
    private String userName;
    // 密码
    private String password;
    // 姓名
    private String name;
    // 年龄
    private Integer age;
    // 性别，1男性，2女性
    private Integer sex;
    // 出生日期
    private Date birthday;
    // 创建时间
    private Date created;
    // 更新时间
    private Date updated;
    // 备注
    private String note;

}

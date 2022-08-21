package org.hf.springcloud.consumer.service.pojo.entity;

import lombok.Data;

import java.util.Date;

/**
 * <p> 用户表映射实体类 </p>
 * //@Data  //在编译阶段会自动根据注解生成对应的方法，包括get/set/hashCode/toString/equals等方法；
 * @author hufei
 * @date 2022/8/21 17:19
*/
@Data
public class User {
    // id
    private Long id;
    // 用户名
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

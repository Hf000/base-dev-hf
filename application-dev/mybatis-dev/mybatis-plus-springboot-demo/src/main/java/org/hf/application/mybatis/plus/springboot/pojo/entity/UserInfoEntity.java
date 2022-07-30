package org.hf.application.mybatis.plus.springboot.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import org.hf.common.publi.pojo.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
* <p> 用户信息表 </p>
 *
 * @author hf
 * @since 2022-07-31 00:14
*/
@Getter
@Setter
@ToString
@TableName("user_info")
public class UserInfoEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
    * 主键
    */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
    * 用户名
    */
    @TableField("user_name")
    private String userName;

    /**
    * 密码 查询时不返回该字段的值
    */
    @TableField(value = "`password`", select = false)
    private String password;

    @TableField("email")
    private String email;

    /**
    * 姓名
    */
    @TableField("`name`")
    private String name;

    /**
    * 年龄
    */
    @TableField("age")
    private Integer age;

    /**
    * 性别，1男性，2女性
    */
    @TableField("sex")
    private Byte sex;

    /**
    * 出生日期
    */
    @TableField("birthday")
    private LocalDate birthday;

    /**
    * 备注
    */
    @TableField("note")
    private String note;
}

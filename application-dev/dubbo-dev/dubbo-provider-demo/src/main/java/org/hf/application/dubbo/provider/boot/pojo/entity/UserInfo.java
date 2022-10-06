package org.hf.application.dubbo.provider.boot.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 表名：user_info
 * 表注释：用户信息表
 */
@Getter
@Setter
@ToString
@TableName("user_info")
public class UserInfo {
    /**
     * 自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户账号
     */
    @TableField("user_name")
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

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 创建时间
     */
    private Date createdDate;

    /**
     * 更新人
     */
    private String updatedBy;

    /**
     * 更新时间
     */
    private Date updatedDate;

    /**
     * 删除状态:0-正常,1-删除
     */
    private Byte deleteState;
}
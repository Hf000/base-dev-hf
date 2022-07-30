package org.hf.application.mybatis.plus.spring.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hf.common.publi.pojo.entity.BaseEntity;

import java.util.Date;

/**
 * <p> 用户实体 </p>
 *
 * @author hufei
 * @date 2021/2/21 17:43
 * @version 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("user_info")
public class User extends BaseEntity {
    private static final long serialVersionUID = 8642913840669165978L;
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String userName;
    private String password;
    private String email;
    private String name;
    private Integer age;
    private Byte sex;
    private Date birthday;
    private String note;
}

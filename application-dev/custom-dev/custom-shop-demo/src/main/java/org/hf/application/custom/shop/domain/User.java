package org.hf.application.custom.shop.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p> 用户信息实体 </p>
 *
 * @author hufei
 * @date 2022/7/17 20:02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User implements Serializable {
    private static final long serialVersionUID = -7509589271610325605L;
    private String username;
    private String password;
    private String sex;
    private String role;
    private Integer level;
}

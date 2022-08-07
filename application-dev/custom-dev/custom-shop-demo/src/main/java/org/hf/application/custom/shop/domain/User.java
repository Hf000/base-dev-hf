package org.hf.application.custom.shop.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>  </p>
 * @author hufei
 * @date 2022/7/17 20:02
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User implements Serializable {
    private String username;
    private String password;
    private String sex;
    private String role;
    private Integer level;
}

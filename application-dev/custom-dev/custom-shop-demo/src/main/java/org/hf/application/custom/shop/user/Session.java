package org.hf.application.custom.shop.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * <p>  </p>
 * @author hufei
 * @date 2022/7/17 20:04
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public abstract class Session {
    private String username;
    private String name;
    private String sex;
    private String role;
    private Integer level;

    //额外操作
    public abstract void handler();
}

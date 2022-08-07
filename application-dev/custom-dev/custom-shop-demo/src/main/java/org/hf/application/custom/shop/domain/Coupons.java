package org.hf.application.custom.shop.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>  </p>
 * @author hufei
 * @date 2022/7/17 20:01
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Coupons implements Serializable {
    private String id;
    private String username;
    private Integer money;
    private Integer status;  //状态，1：未使用，2：已使用
    private Date useTime;    //使用时间
}

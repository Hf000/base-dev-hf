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
public class Order implements Serializable {
    private String itemId;
    private String id;
    private Integer money;
    private Integer paymoney;
    private Integer status;
    private Integer num;
    private String username;
    private String couponsId;
}
package org.hf.application.custom.shop.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p> 订单信息实体 </p>
 *
 * @author hufei
 * @date 2022/7/17 20:02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Order implements Serializable {
    private static final long serialVersionUID = -2007458118674960737L;
    private String itemId;
    private String id;
    private Integer money;
    private Integer payMoney;
    private Integer status;
    private Integer num;
    private String username;
    private String couponsId;
}
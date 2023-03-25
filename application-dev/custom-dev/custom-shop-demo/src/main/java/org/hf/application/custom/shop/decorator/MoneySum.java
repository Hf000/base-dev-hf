package org.hf.application.custom.shop.decorator;

import org.hf.application.custom.shop.domain.Order;

/**
 * <p> 价格计算接口 </p>
 *
 * @author hufei
 * @date 2022/7/17 19:59
 */
public interface MoneySum {

    /**
     * 订单价格[结算]运算
     * @param order 订单信息
     */
    void money(Order order);

}

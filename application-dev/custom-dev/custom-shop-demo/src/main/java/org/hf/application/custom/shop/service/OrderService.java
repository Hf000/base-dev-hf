package org.hf.application.custom.shop.service;

import org.hf.application.custom.shop.domain.Order;

/**
 * <p>  </p>
 * @author hufei
 * @date 2022/7/17 20:02
*/
public interface OrderService {
    /***
     * 添加订单
     * @param order
     */
    int add(Order order);

    /***
     * 取消订单
     * @param id
     */
    void cancelOrder(String id);
}

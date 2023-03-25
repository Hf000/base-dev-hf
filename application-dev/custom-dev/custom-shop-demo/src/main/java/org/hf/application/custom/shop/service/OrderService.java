package org.hf.application.custom.shop.service;

import org.hf.application.custom.shop.domain.Order;

/**
 * <p> 订单业务接口 </p>
 *
 * @author hufei
 * @date 2022/7/17 20:02
 */
public interface OrderService {

    /**
     * 添加订单
     * @param order 订单信息
     */
    void add(Order order);

    /**
     * 取消订单
     * @param id 订单id
     */
    void cancelOrder(String id);
}

package org.hf.application.custom.shop.dao;

import org.hf.application.custom.shop.domain.Order;

/**
 * <p> 订单数据接口 </p>
 *
 * @author hufei
 * @date 2022/7/17 19:58
 */
public interface OrderDao {

    /**
     * 添加订单
     * @param order 订单信息
     */
    void add(Order order);

    /**
     * 查找订单
     * @param id 订单id
     * @return 订单信息
     */
    Order findById(String id);

    /**
     * 修改订单状态
     * @param id 订单id
     * @param status 订单状态
     */
    void modifyStatus(String id, int status);
}

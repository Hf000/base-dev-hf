package org.hf.application.custom.rpc.order.service;

import org.hf.application.custom.rpc.order.pojo.Item;
import org.hf.application.custom.rpc.order.pojo.Order;

import java.util.List;

public interface OrderService {

    /**
     * 提交订单
     * @param userId    用户ID
     * @param itemList  商品项
     * @return 订单信息
     */
    Order submitOrder(Long userId, List<Item> itemList);
}
package org.hf.application.custom.rpc.order.service;

import org.hf.application.custom.rpc.order.pojo.Item;
import org.hf.application.custom.rpc.order.pojo.Order;

import java.util.List;

public interface OrderService {

    Order submitOrder(Long userId, List<Item> itemList);

}

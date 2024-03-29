package org.hf.application.custom.shop.controller;

import lombok.extern.slf4j.Slf4j;
import org.hf.application.custom.shop.domain.Order;
import org.hf.application.custom.shop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p> 订单 </p>
 *
 * @author hufei
 * @date 2022/7/17 19:57
 */
@Slf4j
@RestController
@RequestMapping(value = "/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 添加订单
     * @param order 订单信息
     * @return String
     */
    @PostMapping(value = "/add")
    public String add(@RequestBody Order order) {
        //添加订单
        orderService.add(order);
        return "SUCCESS";
    }

    /**
     * 取消订单
     * @param id 订单id
     * @return String
     */
    @PutMapping(value = "/cancel/{id}")
    public String cancelOrder(@PathVariable(value = "id") String id) {
        //取消订单
        orderService.cancelOrder(id);
        return "SUCCESS";
    }
}

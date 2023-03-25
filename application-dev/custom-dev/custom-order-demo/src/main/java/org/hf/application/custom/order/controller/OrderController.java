package org.hf.application.custom.order.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hf.application.custom.order.mapper.OrdersMapper;
import org.hf.application.custom.order.pojo.dto.OrderDto;
import org.hf.application.custom.order.component.OrderMonitor;
import org.hf.application.custom.order.pojo.entity.Orders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Random;

/**
 * <p> 订单 </p>
 *
 * @author hufei
 * @date 2023/3/25 17:40
 */
@Api(value = "多线程取消超时订单demo")
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OrderMonitor orderMonitor;

    @ApiOperation("下订单")
    @PostMapping("/add")
    @Transactional(rollbackFor = Exception.class)
    public int add(@RequestParam String name) {
        Orders order = new Orders();
        order.setName(name);
        order.setCreatetime(new Date());
        order.setUpdatetime(new Date());
        //超时时间，取10-20之间的随机数（秒）
        order.setInvalid(new Random().nextInt(10) + 10);
        order.setStatus(0);
        ordersMapper.insert(order);
        //事务性验证
//        int i = 1/0;
        // 在下单业务中, 同时扔到同一个队列, 注意事务性
        orderMonitor.put(new OrderDto(order));
        return order.getId();
    }
}

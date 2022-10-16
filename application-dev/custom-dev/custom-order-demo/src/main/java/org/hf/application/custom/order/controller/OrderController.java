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

@RestController
@RequestMapping("/order")
@Api(value = "多线程取消超时订单demo")
public class OrderController {
    @Autowired
    OrdersMapper mapper;
    @Autowired
    OrderMonitor monitor;

    @PostMapping("/add")
    @Transactional
    @ApiOperation("下订单")
    public int add(@RequestParam String name){
        Orders order = new Orders();
        order.setName(name);
        order.setCreatetime(new Date());
        order.setUpdatetime(new Date());
        //超时时间，取10-20之间的随机数（秒）
        order.setInvalid(new Random().nextInt(10)+10);
        order.setStatus(0);
        mapper.insert(order);
        //事务性验证
//        int i = 1/0;
        monitor.put(new OrderDto(order));
        return order.getId();
    }

}

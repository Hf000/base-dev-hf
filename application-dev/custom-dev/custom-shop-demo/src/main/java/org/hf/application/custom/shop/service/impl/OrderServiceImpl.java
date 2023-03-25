package org.hf.application.custom.shop.service.impl;

import org.hf.application.custom.shop.dao.OrderDao;
import org.hf.application.custom.shop.decorator.DecoratorMoneySum;
import org.hf.application.custom.shop.decorator.MoneySum;
import org.hf.application.custom.shop.domain.Order;
import org.hf.application.custom.shop.service.ItemService;
import org.hf.application.custom.shop.service.OrderService;
import org.hf.application.custom.shop.user.SessionThreadLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p> 订单业务接口实现 </p>
 *
 * @author hufei
 * @date 2022/7/17 19:56
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ItemService itemService;

    @Autowired
    private SessionThreadLocal sessionThreadLocal;

    /**
     * ①原始价格计算
     */
    @Autowired
    private MoneySum orderMoneySum;

    /**
     * ②满减计算
     */
    @Autowired
    private DecoratorMoneySum fullMoneySum;

    /**
     * ③Vip价格计算
     */
    @Autowired
    private DecoratorMoneySum vipOrderMoney;

    @Override
    public void add(Order order) {
        //通过享元模式共享获取线程中的对象
        order.setUsername(sessionThreadLocal.get().getUsername());
        //结算价格嵌套运算 1.对orderMoneySum进行增强【计算基础价格】,执行满减操作增强
        fullMoneySum.setMoneySum(orderMoneySum);
        //2.对fullMoneySum进行增强【满减操作】，执行的增强是Vip价格计算
        vipOrderMoney.setMoneySum(fullMoneySum);
        vipOrderMoney.money(order);
        //修改库存
        itemService.modify(order.getNum(), order.getItemId());
        //添加订单
        orderDao.add(order);
    }

    @Override
    public void cancelOrder(String id) {
        //修改订单状态
        Order order = orderDao.findById(id);
        if (order == null) {
            throw new RuntimeException("订单信息异常");
        }
        //改变订单状态
        orderDao.modifyStatus(id, 2);
    }
}

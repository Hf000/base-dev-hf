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
 * <p>  </p>
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

    //①原始价格计算
    @Autowired
    private MoneySum orderMoneySum;
    //②满减计算
    @Autowired
    private DecoratorMoneySum fullMoneySum;
    //③Vip价格计算
    @Autowired
    private DecoratorMoneySum vipOrderMoney;

    /***
     * 取消订单
     * @param id
     */
    @Override
    public void cancelOrder(String id) {
        //修改订单状态
        Order order = orderDao.findById(id);
        orderDao.modifyStatus(id,2);

        //改变订单状态
    }


    /***
     * 添加订单
     * @param order
     */
    @Override
    public int add(Order order) {
        //order.setUsername("wangwu");
        //order.setPaymoney(100); //结算价格
        //order.setMoney(100);  //订单价格

        //通过享元模式共享获取线程中的对象
        order.setUsername(sessionThreadLocal.get().getUsername());

        //结算价格嵌套运算
        fullMoneySum.setMoneySum(orderMoneySum);  //对orderMoneySum进行增强【计算基础价格】,执行满减操作增强
        vipOrderMoney.setMoneySum(fullMoneySum);  //对fullMoneySum进行增强【满减操作】，执行的增强是Vip价格计算
        vipOrderMoney.money(order);

        //修改库存
        int mCount = itemService.modify(order.getNum(), order.getItemId());
        //添加订单
        int addCount = orderDao.add(order);
        return addCount;
    }


}

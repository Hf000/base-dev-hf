package org.hf.application.custom.shop.decorator;

import org.hf.application.custom.shop.domain.Order;
import org.springframework.stereotype.Component;

/**
 * <p> 价格计算 -- 满减价格计算 </p>
 *
 * @author hufei
 * @date 2022/7/17 19:59
 */
@Component(value = "fullMoneySum")
public class FullMoneySum extends DecoratorMoneySum {

    @Override
    public void money(Order order) {
        //被增强的对象方法调用
        super.money(order);
        //执行增强
        fullMoney(order);
    }

    /**
     * 满减价格计算逻辑
     * @param order 订单信息
     */
    public void fullMoney(Order order) {
        if (order.getMoney() >= 100) {
            order.setPayMoney(order.getPayMoney() - 10);
        }
    }
}

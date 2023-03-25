package org.hf.application.custom.shop.decorator;

import org.hf.application.custom.shop.domain.Order;
import org.hf.application.custom.shop.strategy.StrategyFactory;
import org.hf.application.custom.shop.strategy.VipMoney;
import org.hf.application.custom.shop.user.SessionThreadLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p> 价格计算 -- vip价格计算 </p>
 *
 * @author hufei
 * @date 2022/7/17 19:59
 */
@Component(value = "vipOrderMoney")
public class VipOrderMoney extends DecoratorMoneySum {

    @Autowired
    private SessionThreadLocal sessionThreadLocal;

    @Autowired
    private StrategyFactory strategyFactory;

    @Override
    public void money(Order order) {
        //调用被增强的对象方法
        super.money(order);
        //增强
        vipMoney(order);
    }

    /**
     * 根据vip等级计算价格
     * @param order 订单信息
     */
    public void vipMoney(Order order) {
        //获取用户等级
        Integer level = sessionThreadLocal.get().getLevel();
        //获取价格优惠策略
        VipMoney vipMoney = strategyFactory.get(level);
        Integer payMoney = vipMoney.money(order.getPayMoney());
        order.setPayMoney(payMoney);
    }
}

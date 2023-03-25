package org.hf.application.custom.shop.decorator;

import org.hf.application.custom.shop.domain.Order;

/**
 * <p> 价格计算抽象类 </p>
 *
 * @author hufei
 * @date 2022/7/17 20:00
 */
public abstract class DecoratorMoneySum implements MoneySum {

    /**
     * 被扩展的对象
     */
    private MoneySum moneySum;

    public void setMoneySum(MoneySum moneySum) {
        this.moneySum = moneySum;
    }

    @Override
    public void money(Order order) {
        moneySum.money(order);
    }
}

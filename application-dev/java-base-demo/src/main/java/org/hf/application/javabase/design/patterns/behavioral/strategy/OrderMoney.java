package org.hf.application.javabase.design.patterns.behavioral.strategy;

/**
 * <p> 策略模式 </p>
 *
 * @author hufei
 * @date 2022/7/13 21:30
 */
public class OrderMoney {

    /**
     * 将策略接口作为属性
     */
    private Strategy strategy;

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    /**
     * 价格计算
     */
    public Integer moneySum(Integer money) {
        return strategy.money(money);
    }
}

package org.hf.application.javabase.design.patterns.behavioral.strategy;

/**
 * <p>  </p>
 * @author hufei
 * @date 2022/8/7 15:49
*/
public class StrategyTest {

    public static void main(String[] args) {
        //创建对象实例
        OrderMoney orderMoney = new OrderMoney();
        //获取指定策略
        Strategy strategy = StrategyFactory.get(3);
        //设置策略
        orderMoney.setStrategy(strategy);

        //执行计算[基于不同策略计算]
        Integer money = orderMoney.moneySum(1000);
        System.out.println(money);
    }
}

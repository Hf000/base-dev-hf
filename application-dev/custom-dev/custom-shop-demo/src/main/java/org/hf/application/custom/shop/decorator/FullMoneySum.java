package org.hf.application.custom.shop.decorator;

import org.hf.application.custom.shop.domain.Order;
import org.springframework.stereotype.Component;

/**
 * <p>  </p>
 * @author hufei
 * @date 2022/7/17 19:59
*/
@Component(value = "fullMoneySum")
public class FullMoneySum extends DecoratorMoneySum {

    //价格计算
    @Override
    public void money(Order order) {
        //被增强的对象方法调用
        super.money(order);

        //执行增强
        fullMoney(order);
    }


    //满100 减10块
    public void fullMoney(Order order){
        if(order.getMoney()>=100){
            order.setPaymoney(order.getPaymoney()-10);
        }
    }
}

package org.hf.application.custom.shop.strategy;

import org.springframework.stereotype.Component;

/**
 * <p>  </p>
 * @author hufei
 * @date 2022/7/17 20:04
*/
@Component(value = "vipTwo")
public class VipTwo implements VipMoney {

    //Vip2价格计算
    @Override
    public Integer money(Integer money) {
        return money-5;
    }
}

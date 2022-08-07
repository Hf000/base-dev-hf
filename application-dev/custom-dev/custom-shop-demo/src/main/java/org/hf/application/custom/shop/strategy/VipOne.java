package org.hf.application.custom.shop.strategy;

import org.springframework.stereotype.Component;

/**
 * <p>  </p>
 * @author hufei
 * @date 2022/7/17 20:05
*/
@Component(value = "vipOne")
public class VipOne implements VipMoney {

    //Vip1价格计算
    @Override
    public Integer money(Integer money) {
        return money;
    }
}

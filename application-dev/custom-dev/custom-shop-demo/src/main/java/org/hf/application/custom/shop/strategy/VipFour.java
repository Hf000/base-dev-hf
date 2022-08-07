package org.hf.application.custom.shop.strategy;

import org.springframework.stereotype.Component;

/**
 * <p>  </p>
 * @author hufei
 * @date 2022/7/17 20:05
*/
@Component(value = "vipFour")
public class VipFour implements VipMoney {

    //Vip4价格计算
    @Override
    public Integer money(Integer money) {
        return (int)(money*0.1);
    }
}

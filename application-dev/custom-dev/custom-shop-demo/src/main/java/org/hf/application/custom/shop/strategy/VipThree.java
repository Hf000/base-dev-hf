package org.hf.application.custom.shop.strategy;

import org.springframework.stereotype.Component;

/**
 * <p> 会员等级价格计算接口实现3 </p>
 *
 * @author hufei
 * @date 2022/7/17 20:04
 */
@Component(value = "vipThree")
public class VipThree implements VipMoney {

    @Override
    public Integer money(Integer money) {
        return (int) (money * 0.5);
    }
}

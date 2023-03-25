package org.hf.application.custom.shop.strategy;

import org.springframework.stereotype.Component;

/**
 * <p> 会员等级价格计算接口实现1 </p>
 *
 * @author hufei
 * @date 2022/7/17 20:05
 */
@Component(value = "vipOne")
public class VipOne implements VipMoney {

    @Override
    public Integer money(Integer money) {
        return money;
    }
}

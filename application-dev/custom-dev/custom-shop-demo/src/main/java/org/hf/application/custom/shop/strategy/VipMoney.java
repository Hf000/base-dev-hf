package org.hf.application.custom.shop.strategy;

/**
 * <p> 会员等级价格计算接口 </p>
 *
 * @author hufei
 * @date 2022/7/17 20:05
 */
public interface VipMoney {

    /**
     * 金额计算
     * @param money 价格
     * @return 计算后的价格
     */
    Integer money(Integer money);
}

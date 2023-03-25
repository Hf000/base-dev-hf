package org.hf.application.custom.shop.service;

/**
 * <p> 支付业务接口 </p>
 *
 * @author hufei
 * @date 2022/7/17 20:04
 */
public interface PayService {

    /**
     * 支付
     * @param type 支付类型 1:微信支付,2:支付宝支付
     * @param id   订单id
     */
    void pay(String type, String id);
}

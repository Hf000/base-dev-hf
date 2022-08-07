package org.hf.application.custom.shop.service;

/**
 * <p>  </p>
 * @author hufei
 * @date 2022/7/17 20:04
*/
public interface PayService {
    /***
     * 支付
     * @param type
     * @param id
     */
    void pay(String type, String id);
}

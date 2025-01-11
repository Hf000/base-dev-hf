package org.hf.application.javabase.design.patterns.application.demo1;

import org.springframework.stereotype.Service;

/**
 * 支付策略2
 */
@Service
public class WechatPay implements IPayment {

    @Override
    public PayStrategyEnum payType() {
        return PayStrategyEnum.WX;
    }

    @Override
    public Boolean pay(PaymentBody paymentBody) {
        System.out.println("微信支付...");
        return Boolean.TRUE;
    }
}
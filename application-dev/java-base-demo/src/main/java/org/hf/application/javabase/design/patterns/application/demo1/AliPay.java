package org.hf.application.javabase.design.patterns.application.demo1;

import org.springframework.stereotype.Service;

/**
 * 支付策略1
 */
@Service
public class AliPay implements IPayment {

    @Override
    public PayStrategyEnum payType() {
        return PayStrategyEnum.ZFB;
    }

    @Override
    public Boolean pay(PaymentBody paymentBody) {
        System.out.println("支付宝支付...");
        return Boolean.TRUE;
    }
}
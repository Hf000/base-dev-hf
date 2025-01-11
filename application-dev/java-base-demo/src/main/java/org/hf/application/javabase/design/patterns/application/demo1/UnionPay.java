package org.hf.application.javabase.design.patterns.application.demo1;

import org.springframework.stereotype.Service;

/**
 * 支付策略3
 */
@Service
public class UnionPay implements IPayment {

    @Override
    public PayStrategyEnum payType() {
        return PayStrategyEnum.UNION;
    }

    @Override
    public Boolean pay(PaymentBody paymentBody) {
        System.out.println("银联支付...");
        return Boolean.TRUE;
    }
}
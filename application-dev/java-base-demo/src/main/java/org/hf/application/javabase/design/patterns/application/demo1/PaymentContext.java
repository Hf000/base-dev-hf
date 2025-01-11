package org.hf.application.javabase.design.patterns.application.demo1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 支付策略上下文
 */
@Component
public class PaymentContext {

    @Autowired
    private PaymentFactory paymentFactory;

    private IPayment getPayment(PaymentBody paymentBody) {
        return paymentFactory.getPayStrategy(paymentBody.getPayEnum());
    }

    public Boolean pay(PaymentBody paymentBody) {
        IPayment payment = getPayment(paymentBody);
        return payment.pay(paymentBody);
    }
}
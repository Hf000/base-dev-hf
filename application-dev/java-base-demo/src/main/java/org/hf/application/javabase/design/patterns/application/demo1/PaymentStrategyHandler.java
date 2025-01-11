package org.hf.application.javabase.design.patterns.application.demo1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentStrategyHandler {

    @Autowired
    private PaymentContext paymentContext;

    public Boolean pay(PaymentBody payBody) {
        if (payBody == null) {
            throw new IllegalArgumentException("入参异常");
        }
        return paymentContext.pay(payBody);
    }
}
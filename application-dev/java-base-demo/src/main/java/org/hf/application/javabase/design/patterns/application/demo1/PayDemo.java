package org.hf.application.javabase.design.patterns.application.demo1;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * <p> 策略 + 工厂模式进行支付方式策略选择 </p >
 **/
public class PayDemo {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("org.hf.application.javabase.design.patterns.application.demo1");
        PaymentStrategyHandler bean = context.getBean(PaymentStrategyHandler.class);
        PaymentBody paymentBody = new PaymentBody();
        paymentBody.setPayEnum(PayStrategyEnum.WX);
        bean.pay(paymentBody);
    }
}
package org.hf.application.javabase.design.patterns.application.demo1;

/**
 * 支付策略类
 */
public interface IPayment {

    /**
     * 定义支付方式名称
     */
    PayStrategyEnum payType();

    /**
     * 支付
     * @param paymentBody 入参
     */
    Boolean pay(PaymentBody paymentBody);
}
package org.hf.application.javabase.design.patterns.application.demo1;

import cn.hutool.core.map.MapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 创建支付策略工厂
 */
@Component
public class PaymentFactory {

    @Autowired
    private List<IPayment> paymentList;

    private final Map<PayStrategyEnum, IPayment> PAYMENT_MAP = new ConcurrentHashMap<>();

    @PostConstruct
    private void paymentInit() {
        if (MapUtil.isNotEmpty(PAYMENT_MAP)) {
            return;
        }
        paymentList.forEach(payment -> {
            if (PAYMENT_MAP.get(payment.payType()) != null) {
                throw new RuntimeException("支付方式配置异常");
            }
            PAYMENT_MAP.put(payment.payType(), payment);
        });
    }

    public IPayment getPayStrategy(PayStrategyEnum payType) {
        IPayment iPayment = PAYMENT_MAP.get(payType);
        if (iPayment == null) {
            throw new IllegalArgumentException("不支持的支付方式!");
        }
        return iPayment;
    }
}
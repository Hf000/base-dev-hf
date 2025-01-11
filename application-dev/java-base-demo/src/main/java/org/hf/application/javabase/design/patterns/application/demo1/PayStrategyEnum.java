package org.hf.application.javabase.design.patterns.application.demo1;

import lombok.Getter;

/**
 * 支付策略枚举
 */
@Getter
public enum PayStrategyEnum {

    /**
     * 枚举项
     */
    ZFB("org.hf.application.javabase.design.patterns.application.demo1.AliPay"),
    WX("org.hf.application.javabase.design.patterns.application.demo1.WechatPay"),
    UNION("org.hf.application.javabase.design.patterns.application.demo1.UnionPay");

    final String payObjectClass;

    PayStrategyEnum(String payObjectClass) {
        this.payObjectClass = payObjectClass;
    }
}
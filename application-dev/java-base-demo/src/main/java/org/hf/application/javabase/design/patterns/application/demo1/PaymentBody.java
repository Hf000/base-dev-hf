package org.hf.application.javabase.design.patterns.application.demo1;

import lombok.Data;

/**
 * <p> 支付入参 </p >
 **/
@Data
public class PaymentBody {

    private PayStrategyEnum payEnum;
}
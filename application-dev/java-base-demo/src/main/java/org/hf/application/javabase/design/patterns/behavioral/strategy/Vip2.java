package org.hf.application.javabase.design.patterns.behavioral.strategy;

/**
 * <p>  </p>
 * @author hufei
 * @date 2022/8/7 15:50
*/
public class Vip2 implements Strategy {

    /**
     * Vip2 用户
     */
    @Override
    public Integer money(Integer money) {
        return money-5;
    }
}

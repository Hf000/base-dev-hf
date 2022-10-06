package org.hf.application.javabase.design.patterns.behavioral.strategy;

/**
 * <p>  </p>
 *
 * @author hufei
 * @date 2022/8/7 15:49
 */
public class Vip1 implements Strategy {

    /**
     * Vip1 用户
     */
    @Override
    public Integer money(Integer money) {
        return money;
    }
}

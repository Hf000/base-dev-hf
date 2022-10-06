package org.hf.application.javabase.design.patterns.behavioral.strategy;

/**
 * <p>  </p>
 *
 * @author hufei
 * @date 2022/8/7 15:52
 */
public interface Strategy {

    /**
     * 金额计算
     * @param money 入参
     * @return Integer
     */
    Integer money(Integer money);

}

package org.hf.application.javabase.design.patterns.behavioral.chain;

/**
 * <p> 通用链接口 </p >
 *
 * @author hufei
 * @date 2022-09-26
 **/
public interface ChainHandler {

    /**
     * 设置下一个执行链
     * @param handler 入参
     */
    void setNextHandler(ChainHandler handler);

    /**
     * 是否执行
     * @param amount 入参
     * @return boolean
     */
    boolean handler(int amount);

}
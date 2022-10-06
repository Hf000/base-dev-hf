package org.hf.application.javabase.design.patterns.behavioral.chain;

import java.util.Objects;

/**
 * <p> 执行处理器 </p >
 *
 * @author hufei
 * @date 2022-09-26
 **/
public class OneChainHandler implements ChainHandler {

    private ChainHandler chainHandler;

    @Override
    public void setNextHandler(ChainHandler handler) {
        this.chainHandler = handler;
    }

    @Override
    public boolean handler(int amount) {
        Objects.requireNonNull(chainHandler);
        if (amount > 10) {
            System.out.println("第一节点允许执行");
            return true;
        }
        System.out.println("第一节点不允许执行, 判断下一个执行");
        return chainHandler.handler(amount);
    }
}

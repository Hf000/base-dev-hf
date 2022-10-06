package org.hf.application.javabase.design.patterns.behavioral.chain;

/**
 * <p> 执行处理器 </p >
 *
 * @author hufei
 * @date 2022-09-26
 **/
public class TwoChainHandler implements ChainHandler {

    private ChainHandler chainHandler;

    @Override
    public void setNextHandler(ChainHandler handler) {
        this.chainHandler = handler;
    }

    @Override
    public boolean handler(int amount) {
        if (amount > 100) {
            System.out.println("第二节点允许执行");
            return true;
        }
        if (chainHandler != null) {
            System.out.println("第二节点不允许执行, 判断下一个执行");
            return chainHandler.handler(amount);
        }
        System.out.println("第二节点不允许执行,没有下一个执行链了");
        return false;
    }
}
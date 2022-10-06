package org.hf.application.javabase.design.patterns.behavioral.chain;

/**
 * <p> 责任链demo </p >
 * 责任链模式：将请求的发送者和接收者解耦，使的多个对象都有处理这个请求的机会。
 *
 * @author hufei
 * @date 2022-09-26
 **/
public class ResponsibilityChainDemo {

    public static void main(String[] args) {
        // 创建个执行对象
        OneChainHandler one = new OneChainHandler();
        TwoChainHandler two = new TwoChainHandler();
        ThreeChainHandler three = new ThreeChainHandler();
        // 设置执行链路
        one.setNextHandler(two);
        two.setNextHandler(three);
        // 执行
        one.handler(2);
    }

}
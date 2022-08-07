package org.hf.application.javabase.design.patterns.structural.proxy.stati;

/**
 * @Author:hufei
 * @CreateTime:2020-06-30
 * @Description:目标类
 */
public class TargetClass implements ProxyInterface {

    @Override
    public void sendMessage() {
        System.out.println("目标类具体需要做的事情!!!");
    }

}

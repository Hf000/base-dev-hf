package org.hf.application.javabase.design.patterns.structural.proxy.stati;

/**
 * <p> 目标类 </p>
 * @author hufei
 * @date 2022/10/6 11:00
*/
public class TargetClass implements ProxyInterface {

    @Override
    public void sendMessage() {
        System.out.println("目标类具体需要做的事情!!!");
    }

}

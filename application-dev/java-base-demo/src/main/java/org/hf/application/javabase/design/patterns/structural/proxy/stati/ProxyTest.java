package org.hf.application.javabase.design.patterns.structural.proxy.stati;

/**
 * @Author:hufei
 * @CreateTime:2020-06-30
 * @Description:测试类
 */
public class ProxyTest {
    public static void main(String[] args) {
        //创建目标类对象
        TargetClass t = new TargetClass();
        //创建代理类对象
        ProxyClass p = new ProxyClass(t);
        p.sendMessage();
    }
}

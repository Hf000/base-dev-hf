package org.hf.application.javabase.design.patterns.structural.proxy.stati;

/**
 * <p> 静态代理demo </p>
 * @author hufei
 * @date 2022/10/6 11:00
*/
public class ProxyDemo {
    public static void main(String[] args) {
        //创建目标类对象
        TargetClass t = new TargetClass();
        //创建代理类对象
        ProxyClass p = new ProxyClass(t);
        p.sendMessage();
    }
}

package org.hf.application.javabase.design.patterns.structural.proxy.stati;

/**
 * @Author:hufei
 * @CreateTime:2020-06-30
 * @Description:代理类
 */
public class ProxyClass implements ProxyInterface {

    private TargetClass target;//声明目标类成员变量

    public ProxyClass(TargetClass targetParam) {  //重写代理类构造, 创建代理类对象时, 对目标类进行赋值
        this.target = targetParam;
    }

    @Override
    public void sendMessage() {
        System.out.println("前置增强: 调用目标类方法之前需要增强的业务逻辑!!!");
        target.sendMessage();
        System.out.println("后置增强: 调用目标类方法之后需要增强的业务逻辑!!!");
    }
}

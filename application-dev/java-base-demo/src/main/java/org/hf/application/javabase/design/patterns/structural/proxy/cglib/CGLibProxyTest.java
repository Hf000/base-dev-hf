package org.hf.application.javabase.design.patterns.structural.proxy.cglib;

import org.springframework.cglib.proxy.Enhancer;


public class CGLibProxyTest {

    public static void main(String[] args) {
        //给指定对象创建代理
        Landlord landlord = new Landlord();

        //1.被代理的对象字节码
        //2.代理的过程实现，需要实现接口MethodInterceptor->Callback
        Landlord proxyLandlord = (Landlord) Enhancer.create(Landlord.class,new SFang(landlord));
        proxyLandlord.pay("赵六");
    }
}

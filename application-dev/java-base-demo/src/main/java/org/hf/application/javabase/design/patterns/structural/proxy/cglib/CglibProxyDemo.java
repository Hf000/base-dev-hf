package org.hf.application.javabase.design.patterns.structural.proxy.cglib;

import org.springframework.cglib.proxy.Enhancer;

/**
 * <p> 代理模式-cglib动态代理 </p>
 * @author hufei
 * @date 2022/10/6 10:46
*/
public class CglibProxyDemo {

    public static void main(String[] args) {
        //给指定对象创建代理
        Landlord landlord = new Landlord();
        //1.被代理的对象字节码
        //2.代理的过程实现，需要实现接口MethodInterceptor->Callback
        Landlord proxyLandlord = (Landlord) Enhancer.create(Landlord.class,new ProxyImpl(landlord));
        proxyLandlord.pay("赵六");
    }
}

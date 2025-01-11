package org.hf.application.javabase.design.patterns.structural.proxy.cglib;

import org.springframework.cglib.proxy.Enhancer;

/**
 * <p> 代理模式-cglib动态代理 </p>
 * cglib动态代理是基于生成一个被代理类的子类，该子类会重写被代理类的所有非final方法
 * cglib是一个强大的, 高性能, 高质量的code生成类库, 可以在运行期扩展Java类与实现Java接口(字节码增强技术)
 * @author hufei
 * @date 2022/10/6 10:46
*/
public class CglibProxyDemo {

    public static void main(String[] args) {
        //给指定对象创建代理
        Landlord landlord = new Landlord();
        //1.被代理的对象字节码
        //2.代理的过程实现，需要实现接口MethodInterceptor->Callback
        //3.Enhancer代理时增强类
        Landlord proxyLandlord = (Landlord) Enhancer.create(Landlord.class,new ProxyImpl(landlord));
        proxyLandlord.pay("赵六");
    }
}

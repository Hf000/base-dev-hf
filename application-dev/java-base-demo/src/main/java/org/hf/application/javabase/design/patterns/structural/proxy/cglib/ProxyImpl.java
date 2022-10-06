package org.hf.application.javabase.design.patterns.structural.proxy.cglib;

import lombok.SneakyThrows;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
* <p> 代理实现 </p>
* @author hufei
* @date 2022/7/13 21:27
*/
public class ProxyImpl implements MethodInterceptor {

    private final Object instance;

    public ProxyImpl(Object instance) {
        this.instance = instance;
    }

    /**
     * 代理的过程
     * @param o          被代理对象
     * @param method     被代理方法
     * @param objects    方法参数
     * @param methodProxy 方法代理
     * @return Object
     */
    @SneakyThrows
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) {
        objects[0]="中介替租户"+objects[0]+"交钱！";
        return method.invoke(instance,objects);
    }
}

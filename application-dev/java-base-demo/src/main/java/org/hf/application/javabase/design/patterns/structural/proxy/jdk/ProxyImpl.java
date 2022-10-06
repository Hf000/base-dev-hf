package org.hf.application.javabase.design.patterns.structural.proxy.jdk;

import lombok.SneakyThrows;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * <p> 代理过程 </p>
 *
 * @author hufei
 * @date 2022/10/6 10:55
 */
public class ProxyImpl implements InvocationHandler {

    private final Object instance;

    public ProxyImpl(Object instance) {
        this.instance = instance;
    }

    /**
     * 代理过程
     * @param proxy    被代理对象
     * @param method   被代理方法
     * @param args     方法参数
     * @return Object
     */
    @SneakyThrows
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        //修改，增强
        args[0] = "中介替用户" + args[0] + "交钱";
        return method.invoke(instance, args);
    }
}

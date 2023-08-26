package org.hf.application.custom.rpc.core.client;

import org.hf.application.custom.rpc.core.util.HessianSerializer;

import java.lang.reflect.Proxy;

/**
 * 通过动态代理生成代理对象
 */
public class BeanFactory {

    private final NettyClient nettyClient;

    public BeanFactory(NettyClient nettyClient) {
        this.nettyClient = nettyClient;
    }

    public <T> T getBean(Class<T> clazz) {
        /*return (T) Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                new Class<?>[]{clazz},
                new ClientInvocationHandler(nettyClient)
        );*/
        return HessianSerializer.castObject(Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class<?>[]{clazz}, new ClientInvocationHandler(nettyClient)), clazz);
    }

}
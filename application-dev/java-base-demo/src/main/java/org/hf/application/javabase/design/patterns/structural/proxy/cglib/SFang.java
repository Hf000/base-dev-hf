package org.hf.application.javabase.design.patterns.structural.proxy.cglib;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
* <p>  </p>
* @author hufei
* @date 2022/7/13 21:27
*/
public class SFang implements MethodInterceptor {

    private Object instance;

    public SFang(Object instance) {
        this.instance = instance;
    }

    //代理的过程
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        objects[0]="S房网替租户"+objects[0]+"交钱！";
        return method.invoke(instance,objects);
    }
}

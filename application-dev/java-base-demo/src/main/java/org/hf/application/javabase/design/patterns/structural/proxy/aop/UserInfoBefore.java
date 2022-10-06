package org.hf.application.javabase.design.patterns.structural.proxy.aop;

import lombok.SneakyThrows;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.lang.NonNull;

import java.lang.reflect.Method;

/**
 * <p> 增强实现 </p>
 * @author hufei
 * @date 2022/10/6 10:32
*/
public class UserInfoBefore implements MethodBeforeAdvice {

    /**
     * MethodBeforeAdvice:前置通知
     * @param method 被代理方法
     * @param args   方法参数
     * @param target 被代理对象
     */
    @SneakyThrows
    @Override
    public void before(@NonNull Method method, Object[] args, Object target) {
        System.out.println(args[0]+"身份识别通过！");
    }
}

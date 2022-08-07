package org.hf.application.javabase.design.patterns.structural.adapter;

import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;


public class UserInfoBefore implements MethodBeforeAdvice {

    //MethodBeforeAdvice:前置通知
    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println(args[0]+"身份识别通过！");
    }
}

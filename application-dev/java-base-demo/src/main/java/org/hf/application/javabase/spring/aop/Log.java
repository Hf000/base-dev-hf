package org.hf.application.javabase.spring.aop;

import org.aspectj.lang.JoinPoint;


public class Log {

    public void before(JoinPoint jp){
        System.out.println("执行对象："+jp.getSignature().getName());
        System.out.println("前置通知记录日志！");
    }
}

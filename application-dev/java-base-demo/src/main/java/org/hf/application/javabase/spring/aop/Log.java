package org.hf.application.javabase.spring.aop;

import org.aspectj.lang.JoinPoint;

/**
 * <p> 切面增强类 </p>
 *
 * @author hufei
 * @date 2022/9/3 17:02
 */
public class Log {

    public void before(JoinPoint jp) {
        System.out.println("执行对象：" + jp.getSignature().getName());
        System.out.println("前置通知记录日志！");
    }
}

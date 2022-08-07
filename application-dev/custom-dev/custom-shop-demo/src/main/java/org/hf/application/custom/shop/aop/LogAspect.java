package org.hf.application.custom.shop.aop;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hf.application.custom.shop.user.SessionThreadLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>  </p>
 * @author hufei
 * @date 2022/7/17 19:55
*/
@Component
@Aspect
@Slf4j
public class LogAspect {

    @Autowired
    private SessionThreadLocal sessionThreadLocal;

    /***
     * 记录日志
     */
    @SneakyThrows
    @Before("execution(int org.hf.application.custom.shop.service.impl.*.*(..))")
    public void logRecode(JoinPoint joinPoint){
        //获取方法名字和参数
        String methodName = joinPoint.getTarget().getClass().getName()+"."+joinPoint.getSignature().getName();
        //记录日志
        log.info("用户【"+sessionThreadLocal.get().toString()+"】访问："+methodName);
    }

    /****
     * 参数获取
     */
    public String args(Object[] args){
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i <args.length ; i++) {
            buffer.append("  args("+i+"):"+args[i].toString());
        }
        return buffer.toString();
    }
}

package org.hf.common.web.currentlimiting.semaphore;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

/**
 * 基于java自带的Semaphore信号量做限流注解的aop实现
 * 实现原理: Semaphore内部维护了一个计数器,表示当前可用的许可证数量,通过AQS(抽象队列式同步器)实现Semaphore信号量在线程间的同步共享
 * 这里的思想是让请求匀速执行,而不是直接截断请求
 * @author HF
 */
@Aspect
@Component
@Slf4j
public class SemaphoreLimiterAop {
 
    private final Map<String, Semaphore> semaphoreMap = new ConcurrentHashMap<>();
 
    @Pointcut("@annotation(org.hf.common.web.currentlimiting.semaphore.ShLimiter)")
    public void aspect() {
    }
 
    @Around(value = "aspect()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        // 获取当前拦截的方法对象
        Object target = point.getTarget();
        String targetName = target.getClass().getName();
        String methodName = point.getSignature().getName();
        Object[] arguments = point.getArgs();
        Class<?> targetClass = Class.forName(targetName);
        Class<?>[] argTypes = ReflectUtils.getClasses(arguments);
        Method method = targetClass.getDeclaredMethod(methodName, argTypes);
        // 获取目标method上的限流注解@Limiter
        ShLimiter limiter = method.getAnnotation(ShLimiter.class);
        Object result;
        if (null != limiter) {
            // 如果limitType为空则以 class + method + parameters为key，避免重载、重写带来的混乱
            String limitType = limiter.limitType();
            String key = StringUtils.isNotBlank(limitType) ? limitType : targetName + "." + methodName + Arrays.toString(argTypes);
            // 获取限定的流量
            Semaphore semaphore = semaphoreMap.get(key);
            if (null == semaphore) {
                semaphoreMap.putIfAbsent(key, new Semaphore(limiter.limitCount()));
                semaphore = semaphoreMap.get(key);
            }
            try {
                // 获取许可证,获取成功则返回,失败则加入阻塞队列,挂起线程
                log.info("测试是否阻塞");
                semaphore.acquire();
                log.info("测试是否执行");
                result = point.proceed();
            } catch (InterruptedException e) {
                throw new RuntimeException("当前线程被中断了");
            } finally {
                if (null != semaphore) {
                    // 释放许可证, 唤醒阻塞线程
                    semaphore.release();
                }
            }
        } else {
            result = point.proceed();
        }
        return result;
    }

}
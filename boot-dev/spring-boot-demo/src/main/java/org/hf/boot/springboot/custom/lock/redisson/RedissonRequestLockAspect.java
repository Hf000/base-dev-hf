package org.hf.boot.springboot.custom.lock.redisson;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.hf.boot.springboot.error.BusinessException;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Method;

/**
 * 分布式锁实现
 */
@Aspect
@Configuration
@Order(2)
@ConditionalOnProperty(name = "redis.enable", havingValue = "true")
public class RedissonRequestLockAspect {

    @Autowired
    private RedissonClient redissonClient;

    @Around("execution(public * * (..)) && @annotation(org.hf.boot.springboot.custom.lock.redisson.RequestLock)")
    public Object interceptor(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        RequestLock requestLock = method.getAnnotation(RequestLock.class);
        if (StringUtils.isBlank(requestLock.prefix())) {
            throw new BusinessException("重复提交前缀不能为空");
        }
        //获取自定义key
        final String lockKey = RequestKeyGenerator.getLockKey(joinPoint);
        // 使用Redisson分布式锁的方式判断是否重复提交
        RLock lock = redissonClient.getLock(lockKey);
        boolean isLocked = false;
        try {
            //尝试抢占锁
            isLocked = lock.tryLock();
            //没有拿到锁说明已经有了请求了
            if (!isLocked) {
                throw new BusinessException("您的操作太快了,请稍后重试");
            }
            //拿到锁后设置过期时间
            lock.lock(requestLock.expire(), requestLock.timeUnit());
            try {
                return joinPoint.proceed();
            } catch (Throwable throwable) {
                throw new BusinessException("系统异常");
            }
        } catch (Exception e) {
            throw new BusinessException("您的操作太快了,请稍后重试");
        } finally {
            // 释放锁, 判断是否获取到锁并且当前线程是否占用了该锁
            if (isLocked && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }

    }
}
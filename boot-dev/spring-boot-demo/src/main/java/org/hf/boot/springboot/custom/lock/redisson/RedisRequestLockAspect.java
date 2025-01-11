package org.hf.boot.springboot.custom.lock.redisson;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.hf.boot.springboot.error.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;

import java.lang.reflect.Method;

/**
 * 缓存实现
 */
@Aspect
@Configuration
@Order(2)
@ConditionalOnProperty(name = "redis.enable", havingValue = "true")
public class RedisRequestLockAspect {

    @Autowired(required = false)
    private StringRedisTemplate stringRedisTemplate;

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
        // 使用RedisCallback接口执行set命令，设置锁键；设置额外选项：过期时间和SET_IF_ABSENT选项
        final Boolean success = stringRedisTemplate.execute(
            (RedisCallback<Boolean>) connection -> connection.set(lockKey.getBytes(), new byte[0],
                Expiration.from(requestLock.expire(), requestLock.timeUnit()),
                RedisStringCommands.SetOption.SET_IF_ABSENT));
        if (Boolean.FALSE.equals(success)) {
            throw new BusinessException("您的操作太快了,请稍后重试");
        }
        try {
            return joinPoint.proceed();
        } catch (Throwable throwable) {
            throw new BusinessException("系统异常");
        }
    }
}
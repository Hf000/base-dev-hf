package org.hf.boot.springboot.aop;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hf.boot.springboot.error.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * Redis命令拦截切面
 */
@Aspect
@Component
@Slf4j
public class RedisOperationAspect {

    @Value("${redis.command.forbidden:true}")
    private boolean commandForbidden;

    /**
     * 需要禁止的Redis命令
     */
    private final Set<String> forbiddenCommandsSet = new HashSet<>(Lists.newArrayList("KEYS"));

    /**
     * 在执行Redis命令前进行拦截和检查。
     * @param joinPoint 拦截的切点信息
     */
    @Before("execution(* org.springframework.data.redis.core.RedisOperations.*(..))")
    public void beforeRedisCommand(JoinPoint joinPoint) {
        if (!commandForbidden) {
            log.debug("RedisOperationAspect Redis命令拦截开关已关闭。");
            return;
        }
        // 获取方法名称和参数 
        String methodName = joinPoint.getSignature().getName();
        // 检查命令是否在禁止列表中
        if (forbiddenCommandsSet.contains(methodName.toUpperCase())) {
            log.warn("execute forbidden Redis command: {}", methodName);
            throw new BusinessException("Redis命令已禁用");
        }
    }
}
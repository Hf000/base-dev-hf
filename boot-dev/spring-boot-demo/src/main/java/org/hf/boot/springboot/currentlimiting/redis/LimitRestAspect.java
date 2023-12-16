package org.hf.boot.springboot.currentlimiting.redis;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 基于redis+lua限流实现注解 - 4
 */
@Aspect
@Configuration
@Slf4j
@ConditionalOnProperty(name = "redis.enable", havingValue = "true")
public class LimitRestAspect {
 
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
 
    @Autowired
    private DefaultRedisScript<Number> redisLuaScript;

    @Pointcut(value = "@annotation(org.hf.boot.springboot.currentlimiting.redis.RedisLimitAnnotation)")
    public void rateLimit() {
    }
 
    @Around("rateLimit()")
    public Object interceptor(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取当前被拦截的方法对象
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Class<?> targetClass = method.getDeclaringClass();
        // 方法上的获取注解
        RedisLimitAnnotation rateLimit = method.getAnnotation(RedisLimitAnnotation.class);
        if (rateLimit != null) {
            // 拼接请求key
            HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
            String ipAddress = getIpAddress(request);
            String reqKey = ipAddress + "-" + targetClass.getName() + "- " + method.getName() + "-" + rateLimit.key();
            List<String> keys = Collections.singletonList(reqKey);
            //调用lua脚本，获取返回结果，这里即为请求的次数
            Number number = redisTemplate.execute(redisLuaScript, keys, rateLimit.count(), rateLimit.period());
            if (number != null && number.intValue() != 0 && number.intValue() <= rateLimit.count()) {
                log.info("限流时间段内访问了第：{} 次", number);
                return joinPoint.proceed();
            }
        } else {
            return joinPoint.proceed();
        }
        throw new RuntimeException("访问频率过快，被限流了");
    }

    /**
     * 获取请求的IP方法
     */
    private static String getIpAddress(HttpServletRequest request) {
        String ipAddress = null;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null && ipAddress.length() > 15) {
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress = "";
        }
        return ipAddress;
    }
}
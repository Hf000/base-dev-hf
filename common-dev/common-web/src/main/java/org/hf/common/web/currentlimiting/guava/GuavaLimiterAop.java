package org.hf.common.web.currentlimiting.guava;

import com.alibaba.fastjson2.JSONObject;
import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基于guava的令牌桶限流注解aop实现
 * 实现原理: 使用guava的令牌桶算法获取一个令牌，获取不到先等待
 * @author HF
 */
@Aspect
@Component
@Slf4j
public class GuavaLimiterAop {

    /**
     * 使用@SuppressWarnings("all")是因为RateLimiter被标记为了@Beta, 标识可能在未来版本有改变
     */
    @SuppressWarnings("all")
    private final Map<String, RateLimiter> rateLimiterMap = new ConcurrentHashMap<>();

    @Pointcut("@annotation(org.hf.common.web.currentlimiting.guava.TokenBucketLimiter)")
    public void aspect() {
    }

    @SuppressWarnings("all")
    @Around(value = "aspect()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        // 获取当前方法对象
        Object target = point.getTarget();
        String targetName = target.getClass().getName();
        String methodName = point.getSignature().getName();
        Object[] arguments = point.getArgs();
        Class<?> targetClass = Class.forName(targetName);
        Class<?>[] argTypes = ReflectUtils.getClasses(arguments);
        Method method = targetClass.getDeclaredMethod(methodName, argTypes);
        // 获取目标method上的限流注解@TokenBucketLimiter
        TokenBucketLimiter limiter = method.getAnnotation(TokenBucketLimiter.class);
        RateLimiter rateLimiter = null;
        Object result = null;
        if (null != limiter) {
            // 如果limitType为空则以 class + method + parameters为key，避免重载、重写带来的混乱
            String limitType = limiter.limitType();
            String key = StringUtils.isNotBlank(limitType) ? limitType : targetName + "." + methodName + Arrays.toString(argTypes);
            // 根据指定的key去获取令牌
            rateLimiter = rateLimiterMap.get(key);
            if (null == rateLimiter) {
                // 获取限定的流量  为了防止并发
                rateLimiterMap.putIfAbsent(key, RateLimiter.create(limiter.limitCount()));
                rateLimiter = rateLimiterMap.get(key);
            }
            // 判断是否获取到令牌
            if (rateLimiter.tryAcquire()) {
                result = point.proceed();
            } else {
                HttpServletResponse resp = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getResponse();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("success", false);
                jsonObject.put("msg", "限流中");
                try {
                    if (resp != null) {
                        output(resp, jsonObject.toJSONString());
                    }
                } catch (Exception e) {
                    log.error("error,", e);
                }
            }
        } else {
            result = point.proceed();
        }
        return result;
    }

    public void output(HttpServletResponse response, String msg) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            outputStream.write(msg.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                outputStream.flush();
                outputStream.close();
            }
        }
    }
}
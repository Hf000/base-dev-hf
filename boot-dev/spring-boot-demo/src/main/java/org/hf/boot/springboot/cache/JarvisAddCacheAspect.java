package org.hf.boot.springboot.cache;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.hf.boot.springboot.error.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author HF
 */
@Aspect
@Slf4j
@Component
public class JarvisAddCacheAspect {

    @Autowired
    private RedisUtilComponent redisUtilComponent;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Pointcut("@annotation(org.hf.boot.springboot.cache.CustomAddCache)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object jarvisCacheScene(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        CustomAddCache jarvisAddCache = signature.getMethod().getDeclaredAnnotation(CustomAddCache.class);
        // 缓存场景key
        String cacheSceneKey = jarvisAddCache.cacheSceneKey();
        int expireTime = jarvisAddCache.expireTime();
        // 获取当前切面的类名称
        String beanName = signature.getDeclaringType().getSimpleName();
        // 获取切面方法名称
        String methodName = signature.getName();
        // 获取方法传参
        Object[] paramsArray = joinPoint.getArgs();
        String cacheKey = RedisUtilComponent.generateKey(beanName, methodName, paramsArray);
        Boolean sisMember = redisUtilComponent.sisMember(cacheSceneKey, cacheKey);
        if (Boolean.TRUE.equals(sisMember)) {
            Object obj = null;
            try {
                // 这里存缓存的值时也可以通过序列化成字节的方式保存，取出来的时候再通过反序列化转换成对象，参考：https://blog.csdn.net/jike11231/article/details/117790600
                obj = redisTemplate.opsForValue().get(cacheKey);
            } catch (Exception e) {
                log.error("自定义缓存时获取缓存对象异常", e);
            }
            if (obj != null) {
                return obj;
            }
        } else {
            redisUtilComponent.sadd(cacheSceneKey, cacheKey);
        }
        Object proceed;
        try {
            proceed = joinPoint.proceed();
        } catch (Throwable throwable) {
            throw new BusinessException("自定义缓存时方法查询异常", throwable);
        }
        if (proceed != null) {
            redisTemplate.opsForValue().set(cacheKey, proceed, expireTime, TimeUnit.SECONDS);
        }
        return proceed;
    }
}
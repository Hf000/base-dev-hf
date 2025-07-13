package org.hf.boot.springboot.cache;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author HF
 */
@Aspect
@Slf4j
@Component
public class JarvisDelCacheAspect {

    @Autowired
    private RedisUtilComponent redisUtilComponent;

    @Pointcut("@annotation(org.hf.boot.springboot.cache.CustomDelCache)")
    public void pointcut() {
    }

    @After("pointcut()")
    public void jarvisCacheScene(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        CustomDelCache jarvisDelCache = signature.getMethod().getDeclaredAnnotation(CustomDelCache.class);
        // 缓存场景key
        String cacheSceneKey = jarvisDelCache.cacheSceneKey();
        redisUtilComponent.remove(cacheSceneKey);
    }

}
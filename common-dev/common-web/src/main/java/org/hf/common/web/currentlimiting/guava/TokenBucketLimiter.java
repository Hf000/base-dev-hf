package org.hf.common.web.currentlimiting.guava;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 基于guava的令牌桶限流注解
 * @author HF
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TokenBucketLimiter {

    /**
     * 限流类型
     */
    String limitType() default "";

    /**
     * 限流并发数
     */
    int limitCount() default 50;
}
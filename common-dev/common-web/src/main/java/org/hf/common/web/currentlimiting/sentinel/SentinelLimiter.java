package org.hf.common.web.currentlimiting.sentinel;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 基于sentinel的限流注解
 * @author hf
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SentinelLimiter {

    /**
     * 允许接口并发数
     * @return 并发数
     */
    int limitCount() default 50;

    /**
     * 资源名称
     * @return 资源名称
     */
    String resourceName() default "";
}
package org.hf.boot.springboot.custom.lock.redisson;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 自定义请求防重注解
 */
@Inherited
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestLock {

    /**
     * redis锁前缀
     */
    String prefix();

    /**
     * redis锁时间
     */
    long expire() default 1;

    /**
     * redis锁时间单位
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 入参拼接分隔符
     */
    String delimiter() default "&";
}
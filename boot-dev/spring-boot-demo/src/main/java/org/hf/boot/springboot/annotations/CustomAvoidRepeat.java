package org.hf.boot.springboot.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p> 自定义防重注解 </p >
 * 1.防重注解, 切面: org.hf.boot.springboot.aop.AvoidRepeatAspect
 * 自定义防重复注解借助redis实现 - 1
 * @author hufei
 * @date 2023-04-11
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomAvoidRepeat {

    /**
     * 防重复点击的时间阈值，默认3000ms
     */
    int value() default 3000;

    /**
     * 是否需要用户ID防重，白名单接口请设置为false
     */
    boolean needUserId() default true;
}
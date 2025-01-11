package org.hf.boot.springboot.security.sign;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p> 接口签名验证注解 </p >
 * 此注解用于springMVC接口方法上
 *  接口签名实现 - 1
 * @author HF
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface ApiSignVerification {

    /**
     * 签名过期时间，秒 ms 单位
     */
    long expiryTime() default 60000;
}
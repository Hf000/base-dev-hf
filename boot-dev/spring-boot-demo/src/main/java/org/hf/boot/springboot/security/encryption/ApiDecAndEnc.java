package org.hf.boot.springboot.security.encryption;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p> 接口出入参加解密注解 </p >
 * 此注解用于springMVC接口方法上
 *  接口出入参加解密实现 - 1
 * @author HF
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiDecAndEnc {

}
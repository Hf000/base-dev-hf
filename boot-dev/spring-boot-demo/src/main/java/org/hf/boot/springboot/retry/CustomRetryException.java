package org.hf.boot.springboot.retry;

import org.hf.boot.springboot.constants.RetryTypeEnum;
import org.springframework.web.multipart.MultipartFile;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p> 自定义重试异常 </p >
 * 自定义重试异常实现 - 1
 * @author hufei
 * @date 2023-04-11
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface CustomRetryException {

    /**
     * 服务code
     */
    String serviceCode() default "default";

    /**
     * 重试方式 METHOD或者URL
     */
    RetryTypeEnum retryType() default RetryTypeEnum.METHOD;

    /**
     * 忽略的参数名称
     */
    String[] ignoreParamNames() default {};

    /**
     * 忽略的参数类型
     */
    Class<?>[] ignoreParamTypes() default {MultipartFile.class, MultipartFile[].class};
}
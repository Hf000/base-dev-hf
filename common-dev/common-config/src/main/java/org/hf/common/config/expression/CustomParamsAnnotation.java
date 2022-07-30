package org.hf.common.config.expression;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p> 自定义注解: 获取方法上的参数, 并将参数的值赋值给注解的表达式 </p>
 * 注解CustomParamsAnnotation实现 - 1
 * @author hufei
 * @version 1.0.0
 * @date 2021/10/12 15:29
 */
@Inherited
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomParamsAnnotation {

    String[] value() default "";

}

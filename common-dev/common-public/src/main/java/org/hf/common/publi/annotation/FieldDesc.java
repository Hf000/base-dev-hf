package org.hf.common.publi.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p> 字段描述注解 </p >
 * @author hufei
 * @date 2021/10/9 15:29
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldDesc {

    /**
     * 字段描述
     */
    String value() default "";

}
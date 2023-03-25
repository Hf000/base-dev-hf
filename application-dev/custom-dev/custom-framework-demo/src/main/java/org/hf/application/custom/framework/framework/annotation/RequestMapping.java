package org.hf.application.custom.framework.framework.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p> Controller请求注解 </p>
 *
 * @author hufei
 * @date 2022/7/17 19:27
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {

    /**
     * 定义一个注解的属性，属性名字叫value  default  默认值
     * @return String
     */
    String value() default "";
}

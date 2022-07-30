package org.hf.common.config.replacebean;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p> 自定义spring替换bean注解 </p>
 * 自定义spring容器中替换bean注解CustomBeanReplace实现 - 1
 * @author hufei
 * @version 1.0.0
 * @date 2021/10/9 15:29
 */
@Inherited
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomBeanReplace {

    String value() default "";

}

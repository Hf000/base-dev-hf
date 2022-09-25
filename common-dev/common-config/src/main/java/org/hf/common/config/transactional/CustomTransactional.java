package org.hf.common.config.transactional;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p> 自定义事务提交注解-1 </p >
 * @author hufei
 * @date 2022-09-22
 **/
@Inherited
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomTransactional {

    /**
     * 事务回滚异常
     * @return Class<? extends Throwable>[]
     */
    Class<? extends Throwable>[] rollbackFor() default {Exception.class};

}
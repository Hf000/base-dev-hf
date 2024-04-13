package org.hf.boot.springboot.dynamic.datasource;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义数据源切换注解
 * 自定义多数据源处理 - 5
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface CustomDynamicDataSource {

    /**
     * 默认值为主数据源
     */
    String value() default "master";
}
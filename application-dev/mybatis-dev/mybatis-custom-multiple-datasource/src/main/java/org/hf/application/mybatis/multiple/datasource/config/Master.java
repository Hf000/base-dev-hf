package org.hf.application.mybatis.multiple.datasource.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p> 自定义注解Master，有此注解标识的方法走主数据源查询 </p>
 * 自定义实现多数据源 - 1
 * @author hufei
 * @date 2022/8/13 9:50
*/
@Inherited
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Master {
}

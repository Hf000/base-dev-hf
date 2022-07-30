package org.hf.common.config.mybatis.pagehelper;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p> 自定义mybatis-pagehelper分页注解 </p>
 * 自定义分页注解ServicePagination实现 - 1
 * 基于mybatis框架和pagehelper分页插件
 * @author hufei
 * @version 1.0.0
 * @date 2021/11/2 10:56
 */
@Inherited
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ServicePagination {

    /**第几页的请求参数, 通过获取指定参数名称参数的值**/
    String pageNo() default "pageNo";

    /**分页大小的请求参数**/
    String pageSize() default "pageSize";

}

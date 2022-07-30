package org.hf.common.config.enumerate;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p> java枚举标识, 用于将一个java枚举类以唯一码方式标识起来, 可以给前端提供枚举值映射
 * 结合 JavaEnumCacheManager使用
 * </p>
 * 自定义枚举注解JavaEnum实现 - 1
 * @author hufei
 * @version 1.0.0
 * @date 2021/10/12 21:00
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface JavaEnum {

    /**
     * 枚举唯一标识   对应code
     * @return 唯一标识
     */
    @AliasFor("group")
    String value() default "";


    /**
     * 枚举分组code
     * @return 分组code
     */
    @AliasFor("value")
    String group() default "";

    /**
     * 是否可配置, 如果为是,则表示枚举项是基于本枚举类的可选项来动态配置的 (需要从数据库读取)
     * @return 可配置标识
     */
    boolean configurable() default false;

}

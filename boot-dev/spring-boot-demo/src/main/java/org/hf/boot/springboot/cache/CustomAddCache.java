package org.hf.boot.springboot.cache;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义缓存注解<br/>
 * 这里的实现方式是为了解决@Cacheabled和@CacheEvit的使用风险；风险点：如果缓存是经用缓存前缀，再通过@CacheEvit的allentrist=true清除的话，就会有删除所有缓存的风险
 *
 * @author HF
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface CustomAddCache {

    /**
     * 缓存场景值
     * @return 场景值
     */
    String cacheSceneKey();

    /**
     * 过期时间,单位:秒
     * @return 时间值
     */
    int expireTime();

}
package org.hf.common.config.event;

import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p> 事件监听器注解 </p>
 * 自定义事件分发器实现 - 1
 * @author hufei
 * @version 1.0.0
 * @date 2021/10/12 15:56
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface EventListenerAnnotation {
}

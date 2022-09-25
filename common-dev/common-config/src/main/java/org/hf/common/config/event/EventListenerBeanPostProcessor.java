package org.hf.common.config.event;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

/**
 * <p> 事件监听器前后处理 </p>
 * 自定义事件分发器实现 - 12
 * @author hufei
 * @version 1.0.0
 * @date 2021/10/12 16:09
 */
@Component
@ConditionalOnBean(EventMulticaster.class)
public class EventListenerBeanPostProcessor implements DestructionAwareBeanPostProcessor, BeanPostProcessor {

    private final EventMulticaster eventMulticaster;

    public EventListenerBeanPostProcessor(EventMulticaster eventMulticaster) {
        this.eventMulticaster = eventMulticaster;
    }

    @Override
    public Object postProcessBeforeInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        // 改方法在bean实例化完毕(且已经注入完毕), 在afterPropertiesSet或自定义init方法执行之前
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        // 在afterPropertiesSet或自定义init方法执行之后
        if (eventMulticaster != null && bean instanceof EventListener &&
                AnnotatedElementUtils.hasAnnotation(bean.getClass(), EventListenerAnnotation.class)) {
            eventMulticaster.addListener((EventListener<? extends EventBase>) bean);
        }
        return bean;
    }

    @Override
    public boolean requiresDestruction(@NonNull Object bean) {
        // 对象销毁前的处理, 判断是否需要销毁
        return bean instanceof EventListener && AnnotatedElementUtils.hasAnnotation(bean.getClass(), EventListenerAnnotation.class);
    }

    @Override
    public void postProcessBeforeDestruction(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        // 这里实现对象的销毁逻辑
        if (eventMulticaster != null && bean instanceof EventListener &&
                AnnotatedElementUtils.hasAnnotation(bean.getClass(), EventListenerAnnotation.class)) {
            eventMulticaster.removeListener((EventListener<? extends EventBase>) bean);
        }
    }

    @PreDestroy
    public void preDestroy() {
        // 销毁对象
        if (eventMulticaster != null) {
            eventMulticaster.removeAllListener();
        }
    }
}

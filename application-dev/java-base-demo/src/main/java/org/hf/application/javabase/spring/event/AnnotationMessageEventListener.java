package org.hf.application.javabase.spring.event;

import org.springframework.context.event.EventListener;

/**
 * <p> 自定义事件监听类,通过注解方式实现spring事件监听接口 此方式需要spring版本4.2.0及以上 </p >
 * @author hufei
*/
public class AnnotationMessageEventListener {

    /**
     * 监听触发执行
     * 由于这里的Bean是通过XML方式进行注册, 所以需要在XML文件中通过<context:annotation-config/>激活
     * 原因:通过XML方式配置,Spring默认不会扫描和注册带有@EventListener注解的Bean
     * @param messageEvent 监听到的事件对象
     */
    @EventListener
    public void handlerEvent(MessageEvent messageEvent) {
        System.out.println("注解方式,监听到了对应事件！");
    }

}
package org.hf.application.javabase.spring.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;

/**
 * <p> 自定义事件监听类,需要实现spring事件监听接口 </p >
 * 注意: 这里的接口泛型需要指定成自定义事件对象,如果指定成spring事件对象ApplicationEvent,会触发两次
 * 原因: 是容器初始化完成的时候会监听ApplicationEvent事件,此时会触发一次,后面又push了一次,所以会再次触发
 * @author hufei
 * @date 2022/9/3 17:05
*/
public class MessageEventListener implements ApplicationListener<MessageEvent> {

    /**
     * 监听触发执行
     * @param messageEvent 监听到的事件对象
     */
    @Override
    public void onApplicationEvent(@NonNull MessageEvent messageEvent) {
        System.out.println("监听到了对应事件！");
    }


}

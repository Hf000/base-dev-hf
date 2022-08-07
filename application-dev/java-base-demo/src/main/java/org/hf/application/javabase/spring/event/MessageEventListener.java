package org.hf.application.javabase.spring.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;


public class MessageEventListener implements ApplicationListener<ApplicationEvent> {

    //监听触发执行
    @Override
    public void onApplicationEvent(@NonNull ApplicationEvent applicationEvent) {
        System.out.println("监听到了对应事件！");
    }


}

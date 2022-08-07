package org.hf.application.javabase.spring.event;

import org.springframework.context.ApplicationEvent;


public class MessageEvent extends ApplicationEvent {

    /***
     * 可以用于传递数据
     */
    public MessageEvent(Object source) {
        super(source);
    }
}

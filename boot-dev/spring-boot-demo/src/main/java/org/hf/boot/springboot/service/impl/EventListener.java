package org.hf.boot.springboot.service.impl;

import org.hf.boot.springboot.config.AbstractEventSubscriber;
import org.hf.boot.springboot.pojo.bo.TestEvent;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * <p> 事件订阅实现 </p >
 * @author hufei
 * @date 2022-09-21
 **/
@Component
public class EventListener extends AbstractEventSubscriber<TestEvent> {

    @Override
    public void subscribe(@NonNull TestEvent event) {
        System.out.println("监听到了对应事件！" + event.getName());
    }

}
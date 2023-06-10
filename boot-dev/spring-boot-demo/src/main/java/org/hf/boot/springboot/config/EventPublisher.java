package org.hf.boot.springboot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * <p> 事件发布 </p >
 * 自定义扩展Sprig事件发布订阅实现 - 5
 * @author hufei
 * @date 2022-09-21
 **/
@Slf4j
@Component
public class EventPublisher {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    /**
     * 事件发布（同步）
     * 目前仅支持spring容器内事件订阅
     * @param applicationEvent 事件对象
     */
    public void publishEvent(AbstractEvent applicationEvent){
        applicationEventPublisher.publishEvent(applicationEvent);
    }

    /**
     * 事件发布（异步）
     * 目前仅支持spring容器内事件订阅
     * @param applicationEvent 事件对象
     */
    @Async
    public void asyncPublishEvent(AbstractEvent applicationEvent){
        applicationEventPublisher.publishEvent(applicationEvent);
    }

}

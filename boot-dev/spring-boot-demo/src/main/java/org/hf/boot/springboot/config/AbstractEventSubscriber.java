package org.hf.boot.springboot.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.PayloadApplicationEvent;

/**
 * <p> 事件订阅类 </p >
 * @author hufei
 * @date 2022-09-21
 **/
@Slf4j
public abstract class AbstractEventSubscriber<E> implements ApplicationListener<PayloadApplicationEvent<E>> {

    /**
     * 获取发布的事件对象
     * @param event 事件对象
     */
    @Override
    public void onApplicationEvent(PayloadApplicationEvent<E> event){
        E eventSource = event.getPayload();
        log.info("AbstractEventSubscriber.onApplicationEvent start param：{}",eventSource);
        subscribe(eventSource);
        log.info("AbstractEventSubscriber.onApplicationEvent end");
    }

    /**
     * 事件订阅
     * @param event 事件对象
     */
    abstract public void subscribe(E event);

}
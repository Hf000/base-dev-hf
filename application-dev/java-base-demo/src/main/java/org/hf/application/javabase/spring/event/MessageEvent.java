package org.hf.application.javabase.spring.event;

import org.springframework.context.ApplicationEvent;

/**
 * <p> 自定义监听事件,需要继承spring的时间监听ApplicationEvent </p>
 *
 * @author hufei
 * @date 2022/9/3 17:04
 */
public class MessageEvent extends ApplicationEvent {

    private static final long serialVersionUID = -87522670217219148L;

    /***
     * 可以用于传递数据
     */
    public MessageEvent(Object source) {
        super(source);
    }
}

package org.hf.common.config.event;

/**
 * <p> 事件分发器接口 </p>
 * 自定义事件分发器实现 - 7
 * @author hufei
 * @version 1.0.0
 * @date 2021/10/12 16:11
 */
public interface EventMulticaster {

    /**
     * 添加监听器
     * @param listener 监听器
     */
    void addListener(EventListener<? extends EventBase> listener);

    /**
     * 移除事件监听器
     * @param listener 监听器
     */
    void removeListener(EventListener<? extends EventBase> listener);

    /**
     * 移除所有事件监听器
     */
    void removeAllListener();

    /**
     * 派发事件
     * @param event 事件
     */
    void multicastEvent(EventBase event);

}

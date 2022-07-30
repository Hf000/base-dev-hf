package org.hf.common.config.event;

/**
 * <p> 事件发布实现方式一: 事件发布器接口 </p>
 * 自定义事件分发器实现 - 9
 * @author hufei
 * @version 1.0.0
 * @date 2021/10/12 17:02
 */
public interface EventPublisher {

    /**
     * 发布事件
     * @param event 事件实体
     */
    void publish(EventBase event);

}

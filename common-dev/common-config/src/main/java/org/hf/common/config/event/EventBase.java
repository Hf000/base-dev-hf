package org.hf.common.config.event;

/**
 * <p> 事件基类接口: 自定义事件实体需要实现此接口, 以被事件监听处理器识别监听 </p>
 * 自定义事件分发器实现 - 2
 * @author hufei
 * @version 1.0.0
 * @date 2021/10/12 15:26
 */
public interface EventBase {

    /**
     * 获取事件唯一key
     * @return 返回唯一key
     */
    String getKey();

}

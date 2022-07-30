package org.hf.common.config.event;

import org.springframework.core.Ordered;
import org.springframework.core.ResolvableType;

/**
 * <p> 事件监听实现方式二: 事件监听器抽象类 </p>
 *  在实现方式一的基础上进行了一些默认设置, 也可以直接实现EventListener, 然后自定义默认设置
 *  自定义事件分发器实现 - 5
 * @author hufei
 * @version 1.0.0
 * @date 2021/10/12 15:38
 */
public abstract class EventListenerAbstract<E extends EventBase> implements EventListener<E>, Ordered {

    /**
     * 校验事件实体类是否为支持的事件类型
     * @param eventType 事件类型
     * @return 检验结果
     */
    public boolean supportEventType(ResolvableType eventType) {
        return ResolvableType.forClass(getClass()).as(EventListener.class).getGeneric().isAssignableFrom(eventType);
    }

    /**
     * 返回当前类加载的优先级: 越小的值,优先级越高,反之优先级越低
     * @return 返回当前类加载的优先级
     */
    @Override
    public int getOrder() {
        return  LOWEST_PRECEDENCE;
    }
}

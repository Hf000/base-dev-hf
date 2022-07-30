package org.hf.common.config.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * <p> 事件分发器实现 </p>
 * 自定义事件分发器实现 - 8
 * @author hufei
 * @version 1.0.0
 * @date 2021/10/12 16:35
 */
@Slf4j
@Component
public class EventMulticasterImpl implements EventMulticaster {

    private final Set<EventListener<? extends EventBase>> listeners = new LinkedHashSet<>();

    @Override
    public void addListener(EventListener<? extends EventBase> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(EventListener<? extends EventBase> listener) {
        listeners.remove(listener);
    }

    @Override
    public void removeAllListener() {
        listeners.clear();
    }

    @Override
    public void multicastEvent(EventBase event) {
        log.info("EventMulticasterImpl.multicastEvent event-key:[{}]", event.getKey());
        for (EventListener<? extends EventBase> eventListener : getEventListeners(event)) {
            try {
                // 事件分发
                invokeListener(eventListener, event);
            } catch (Exception e) {
                // 事件分发失败暂未处理, 可根据实际场景实现
                log.error("事件分发器处理事件分发失败", e);
            }
        }
    }

    /**
     * 事件分发
     * @param eventListener     监听处理器
     * @param event             事件
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private void invokeListener(EventListener eventListener, EventBase event) {
        eventListener.onEvent(event);
    }

    /**
     * 获取监听到的事件
     * @param event 事件
     * @return 返回监听到的事件的监听处理器
     */
    private List<EventListener<? extends EventBase>> getEventListeners(EventBase event) {
        // 获取事件类型
        ResolvableType eventType = ResolvableType.forInstance(event);
        // 遍历所有的listener
        Set<EventListener<? extends EventBase>> allListeners = new LinkedHashSet<>();
        for (EventListener<? extends EventBase> listener : listeners) {
            // 判断是否支持当前事件
            if (supportEvent(listener, eventType)) {
                allListeners.add(listener);
            }
        }
        ArrayList<EventListener<? extends EventBase>> eventListeners = new ArrayList<>(allListeners);
        AnnotationAwareOrderComparator.sort(eventListeners);
        return eventListeners;
    }

    /**
     * 判断是否支持监听到的事件类型
     * @param listener  事件监听
     * @param eventType 事件类型
     * @return 检查结果
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private boolean supportEvent(EventListener listener, ResolvableType eventType) {
        if (listener instanceof EventListenerAbstract) {
            return ((EventListenerAbstract<?>)listener).supportEventType(eventType);
        }
        EventListenerAdapter eventListenerAdapter = new EventListenerAdapter(listener);
        return eventListenerAdapter.supportEventType(eventType);
    }
}

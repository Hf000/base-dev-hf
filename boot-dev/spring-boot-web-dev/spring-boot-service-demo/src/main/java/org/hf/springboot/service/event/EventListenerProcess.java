package org.hf.springboot.service.event;

import lombok.extern.slf4j.Slf4j;
import org.hf.common.config.event.EventListenerAbstract;
import org.hf.common.config.event.EventListenerAnnotation;
import org.hf.springboot.service.pojo.bo.EventBO;

/**
 * <p> 监听器实现 </p>
 * 如果不需要默认业务逻辑, 可以直接实现EventListener接口
 * 注解@EventListenerAnnotation修饰的类已交给spring管理可以直接进行bean的注入
 * @author hufei
 * @version 1.0.0
 * @date 2021/10/12 17:17
 */
@Slf4j
@EventListenerAnnotation
public class EventListenerProcess extends EventListenerAbstract<EventBO> {
    @Override
    public void onEvent(EventBO event) {
        log.info("监听测试==>{}", event.toString());
    }
}

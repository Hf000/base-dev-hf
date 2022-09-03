package org.hf.application.javabase.spring.event;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * <p> 自定义事件监听类,需要实现spring事件监听接口 </p>
 * ContextRefreshedEvent,spring容器初始化完成时会触发
 *
 * @author hufei
 * @date 2022/9/3 16:19
 */
public class ApplicationEventListener implements ApplicationListener<ContextRefreshedEvent> {

    /**
     * 发生指定事件后会触发该方法执行
     *
     * @param contextRefreshedEvent 指定事件
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext act = contextRefreshedEvent.getApplicationContext();
        System.out.println("监听到容器初始化完成！这里可以进行自定义业务逻辑,例如从数据库加载对应的数据（省市区）到redis缓存");
    }
}

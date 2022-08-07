package org.hf.application.javabase.spring.event;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;


public class ApplicationEventListener implements ApplicationListener<ContextRefreshedEvent> {

    //发生指定事件后会触发该方法执行
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext act = contextRefreshedEvent.getApplicationContext();
        System.out.println("监听到容器初始化完成！");

        //启动->从数据库加载对应的数据（省市区）->填充Redis

    }
}

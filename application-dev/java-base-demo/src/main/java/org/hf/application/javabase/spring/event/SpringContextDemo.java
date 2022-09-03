package org.hf.application.javabase.spring.event;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * <p> spring时间监听demo </p>
 * @author hufei
 * @date 2022/9/3 17:07
*/
public class SpringContextDemo {

    public static void main(String[] args) {
        // 加载监听实例
        ApplicationContext act = new ClassPathXmlApplicationContext("spring-event.xml");
        //添加一个自定义事件
        act.publishEvent(new MessageEvent("事件对象"));
    }
}

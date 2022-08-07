package org.hf.application.javabase.spring.event;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class SpringContextTest {

    public static void main(String[] args) {
        ApplicationContext act = new ClassPathXmlApplicationContext("spring-event.xml");

        //添加一个自定义事件
        act.publishEvent(new MessageEvent("hello!"));
    }
}

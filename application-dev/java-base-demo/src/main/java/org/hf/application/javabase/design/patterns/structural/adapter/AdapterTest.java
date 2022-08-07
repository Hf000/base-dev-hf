package org.hf.application.javabase.design.patterns.structural.adapter;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
* <p> 适配器模式 </p>
* @author hufei
* @date 2022/7/13 21:20
*/
public class AdapterTest {

    public static void main(String[] args) {
        ApplicationContext act = new ClassPathXmlApplicationContext("spring-adapter.xml");
        UserCardService userCardService = (UserCardService) act.getBean("proxyBean");
        userCardService.card("王五");
    }
}

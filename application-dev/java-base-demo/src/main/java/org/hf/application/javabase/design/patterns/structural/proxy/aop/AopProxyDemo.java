package org.hf.application.javabase.design.patterns.structural.proxy.aop;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
* <p> 通过Aop方式实现代理 </p>
* @author hufei
* @date 2022/7/13 21:20
*/
public class AopProxyDemo {

    public static void main(String[] args) {
        ApplicationContext act = new ClassPathXmlApplicationContext("spring-proxy.xml");
        UserCardService userCardService = (UserCardService) act.getBean("proxyBean");
        userCardService.card("王五");
    }
}

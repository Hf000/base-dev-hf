package org.hf.application.javabase.spring.aop;

import org.hf.application.javabase.spring.aop.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class AopTest {

    public static void main(String[] args) {
        ApplicationContext act = new ClassPathXmlApplicationContext("spring-aop.xml");
        UserService userService = (UserService) act.getBean("userService");
        userService.add();

    }
}

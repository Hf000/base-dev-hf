package org.hf.application.javabase.spring.aop;

import org.hf.application.javabase.spring.aop.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * <p> Spring自定义AOP切面demo </p>
 *
 * @author hufei
 * @date 2022/9/3 16:17
 */
public class AopDemo {

    public static void main(String[] args) {
        ApplicationContext act = new ClassPathXmlApplicationContext("spring-aop.xml");
        UserService userService = (UserService) act.getBean("userService");
        userService.add();
    }
}

package org.hf.application.javabase.spring.load.sequence;

import org.springframework.stereotype.Component;

/**
 * <p> spring容器启动顺序测试5 </p >
 * // @Component注解的初始化优先级大于@Bean注解
 */
@Component
public class TestSpringOrderBean {

    public TestSpringOrderBean() {
        System.out.println("启动顺序4-1-2:构造函数 TestSpringOrderBean");
    }
}
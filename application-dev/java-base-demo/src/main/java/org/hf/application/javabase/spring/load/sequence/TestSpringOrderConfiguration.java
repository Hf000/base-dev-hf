package org.hf.application.javabase.spring.load.sequence;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p> spring容器启动顺序测试3 </p >
 */
@Configuration
public class TestSpringOrderConfiguration {

    @Bean(initMethod = "customInitMethod", destroyMethod = "customDestroyMethod")
    public TestSpringOrder testSpringOrder() {
        System.out.println("启动顺序4-2-1[此顺序依赖bean初始化写的代码优先位置,但是@Component注解的初始化优先级大于@Bean注解]:TestSpringOrder的@Bean注解方法执行");
        return new TestSpringOrder();
    }

    @Bean
    public TestSpringOrderBean testSpringOrderBean() {
        System.out.println("启动顺序4-1-1[此顺序依赖bean初始化写的代码优先位置,但是@Component注解的初始化优先级大于@Bean注解]:TestSpringOrderBean的@Bean注解方法执行");
        return new TestSpringOrderBean();
    }

    /**
     * 这里不手动实例化此对象的原因:如果该对象在这里实例化,就会导致TestSpringOrderConfiguration在TestSpringOrderBeanProcessor
     * 之前实例化,而错过BeanPostProcessor的处理,所以将TestSpringOrderBeanProcessor交给spring去实例化就可以避免这个问题
     */
    /*@Bean
    public TestSpringOrderBeanProcessor testSpringOrderBeanProcessor() {
        return new TestSpringOrderBeanProcessor();
    }*/
}
package org.hf.application.javabase.spring.load.sequence;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * <p> spring容器启动顺序测试1 </p >
 * 执行结果:
 * 启动顺序1:容器初始化之前...
 * 启动顺序2:BeanFactoryPostProcessor postProcessBeanFactory
 * 启动顺序3:@Bean 注解方法执行
 * 启动顺序3:@Bean 注解方法执行
 * 启动顺序4:构造函数 TestSpringOrder
 * 启动顺序5: Autowired
 * 启动顺序6:BeanNameAware setBeanName
 * 启动顺序7:BeanFactoryAware setBeanFactory
 * 启动顺序8:ApplicationContextAware setApplicationContext
 * 启动顺序9:BeanPostProcessor postProcessBeforeInitialization beanName:testSpringOrder
 * 启动顺序10:post-construct
 * 启动顺序11:InitializingBean afterPropertiesSet
 * 启动顺序12:TestSpringOrder customInitMethod
 * 启动顺序13:BeanPostProcessor postProcessAfterInitialization beanName:{}testSpringOrder
 * 启动顺序14:SmartInitializingSingleton afterSingletonsInstantiated
 * 启动顺序15和21:Lifecycle isRunning
 * 启动顺序16:Lifecycle start
 * 启动顺序17:onApplicationEvent ContextRefreshedEvent
 * 启动顺序18:容器初始化完成
 * 启动顺序19:CommandLineRunner run
 * 启动顺序20:容器准备关闭...
 * 启动顺序15和21:Lifecycle isRunning
 * 启动顺序22:Lifecycle stop
 * 启动顺序23:preDestroy
 * 启动顺序24:DisposableBean destroy
 * 启动顺序25:TestSpringOrder customDestroyMethod
 * 启动顺序26:容器已经关闭
 **/
public class TestSpringApplicationDemo {

    public static void main(String[] args) {
        System.out.println("启动顺序1:容器初始化之前...");
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("org.hf.application.javabase.spring.load.sequence");
        System.out.println("启动顺序18:容器初始化完成");
        TestSpringOrder bean = context.getBean(TestSpringOrder.class);
        bean.run();
        System.out.println("启动顺序20:容器准备关闭...");
        context.close();
        System.out.println("启动顺序26:容器已经关闭");
    }
}
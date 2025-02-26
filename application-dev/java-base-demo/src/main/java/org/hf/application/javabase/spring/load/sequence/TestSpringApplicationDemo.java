package org.hf.application.javabase.spring.load.sequence;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * <p> spring容器启动顺序测试1 </p >
 * 执行结果:
 启动顺序1:容器初始化之前...
 启动顺序2:调用BeanFactoryPostProcessor#postProcessBeanFactory()方法

 {{启动顺序4-1-1[此顺序依赖bean初始化写的代码优先位置,但是@Component注解的初始化优先级大于@Bean注解]:TestSpringOrderBean的@Bean注解方法执行
 启动顺序4-1-2:构造函数 TestSpringOrderBean}
 注意: 这里在启动顺序3之前执行是因为在打印调用链路信息时只打印了类TestSpringOrder相关的,而TestSpringOrderBean在TestSpringOrder之前创建,
 所以结果打印就在前面了}

 启动顺序3:调用InstantiationAwareBeanPostProcessor#postProcessBeforeInstantiation()方法
 启动顺序4-2-1[此顺序依赖bean初始化写的代码优先位置,但是@Component注解的初始化优先级大于@Bean注解]:TestSpringOrder的@Bean注解方法执行
 启动顺序4-2-2:构造函数 TestSpringOrder
 启动顺序5:调用InstantiationAwareBeanPostProcessor#postProcessAfterInstantiation()方法
 启动顺序6:调用InstantiationAwareBeanPostProcessor#postProcessProperties()方法
 启动顺序7:通过注解@Autowired进行依赖的bean注入
 启动顺序8:调用BeanNameAware#setBeanName()方法,进行属性设置
 启动顺序9:调用BeanFactoryAware#setBeanFactory()方法,通过beanFactory实例化bean
 启动顺序10:调用ApplicationContextAware#setApplicationContext()方法,设置spring中bean的上下文
 启动顺序11:调用BeanPostProcessor#postProcessBeforeInitialization()方法,beanName:testSpringOrder
 启动顺序12:通过注解@PostConstruct进行bean属性赋值后处理,当Bean的所有属性被Spring容器注入完成之后调用此时方法
 启动顺序13:调用InitializingBean#afterPropertiesSet()方法,当Bean的所有属性被Spring容器注入完成之后调用此方法进行初始化
 启动顺序14:调用TestSpringOrder#customInitMethod()方法,类指定的初始化方法
 启动顺序15:调用BeanPostProcessor#postProcessAfterInitialization()方法,beanName:{}testSpringOrder
 启动顺序16:调用SmartInitializingSingleton#afterSingletonsInstantiated()方法,所有单例bean初始化完成后进行回调
 启动顺序17和23:调用Lifecycle#isRunning()方法,判断bean状态
 启动顺序18:调用Lifecycle#start(),标记bean状态
 启动顺序19:调用ApplicationListener#onApplicationEvent()方法,进行事件ContextRefreshedEvent的发布监听
 启动顺序20:容器初始化完成
 启动顺序21:调用CommandLineRunner#run()方法,进行应用启动后的加载
 启动顺序22:容器准备关闭...
 启动顺序17和23:调用Lifecycle#isRunning()方法,判断bean状态
 启动顺序24:调用Lifecycle#stop()方法,状态bean标记
 启动顺序25:通过注解@PreDestroy进行bean销毁前的处理
 启动顺序26:调用DisposableBean#destroy()方法,进行bean销毁前处理
 启动顺序27:调用TestSpringOrder#customDestroyMethod()方法,类指定的bean销毁前处理方法
 启动顺序28:容器已经关闭
 **/
public class TestSpringApplicationDemo {

    public static void main(String[] args) {
        System.out.println("启动顺序1:容器初始化之前...");
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("org.hf.application.javabase.spring.load.sequence");
        System.out.println("启动顺序20:容器初始化完成");
        TestSpringOrder bean = context.getBean(TestSpringOrder.class);
        bean.run();
        System.out.println("启动顺序22:容器准备关闭...");
        context.close();
        System.out.println("启动顺序28:容器已经关闭");
    }
}
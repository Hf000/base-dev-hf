package org.hf.application.javabase.spring.load.sequence;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.event.ContextRefreshedEvent;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * <p> spring容器启动顺序测试4 </p >
 */
public class TestSpringOrder implements
        ApplicationContextAware,
        BeanFactoryAware,
        InitializingBean,
        SmartLifecycle,
        BeanNameAware,
        ApplicationListener<ContextRefreshedEvent>,
        CommandLineRunner,
        SmartInitializingSingleton,
        DisposableBean {

    public TestSpringOrder() {
        System.out.println("启动顺序4-2:构造函数 TestSpringOrder");
    }

    @Autowired
    private void setTestSpringOrderBean(TestSpringOrderBean testSpringOrderBean) {
        System.out.println("启动顺序5: Autowired");
    }

    @Override
    public void setBeanName(@NotNull String beanName) {
        // 设置bean在beanFactory中的名称, 这里的入参beanName就是此bean在bean工厂中已有的名称
        System.out.println("启动顺序6:BeanNameAware setBeanName");
    }

    @Override
    public void setBeanFactory(@NotNull BeanFactory beanFactory) throws BeansException {
        // 设置bean的beanFactory的引用, 这里的入参beanFactory就是实例化此bean的bean工厂引用
        System.out.println("启动顺序7:BeanFactoryAware setBeanFactory");
    }

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        System.out.println("启动顺序8:ApplicationContextAware setApplicationContext");
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("启动顺序10:post-construct");
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("启动顺序11:InitializingBean afterPropertiesSet");
    }

    public void customInitMethod() {
        System.out.println("启动顺序12:TestSpringOrder customInitMethod");
    }

    @Override
    public void afterSingletonsInstantiated() {
        System.out.println("启动顺序14:SmartInitializingSingleton afterSingletonsInstantiated");
    }

    private boolean runFlag = false;

    @Override
    public boolean isRunning() {
        System.out.println("启动顺序15和21:Lifecycle isRunning");
        // 这里的返回值是表示当前bean是否开启, 值的返回会影响org.hf.application.javabase.spring.sequence.TestSpringOrder.start()
        // 和org.hf.application.javabase.spring.sequence.TestSpringOrder.stop()方法的调用
        return runFlag;
    }

    @Override
    public void start() {
        runFlag = true;
        System.out.println("启动顺序16:Lifecycle start");
    }

    @Override
    public void onApplicationEvent(@NotNull ContextRefreshedEvent refreshEvent) {
        System.out.println("启动顺序17:onApplicationEvent ContextRefreshedEvent");
    }

    @Override
    public void run(String... args) {
        System.out.println("启动顺序19:CommandLineRunner run");
    }

    @Override
    public void stop() {
        runFlag = false;
        System.out.println("启动顺序22:Lifecycle stop");
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("启动顺序23:preDestroy");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("启动顺序24:DisposableBean destroy");
    }

    public void customDestroyMethod() {
        System.out.println("启动顺序25:TestSpringOrder customDestroyMethod");
    }

}
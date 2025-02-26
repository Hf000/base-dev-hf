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
        System.out.println("启动顺序4-2-2:构造函数 TestSpringOrder");
    }

    @Autowired
    private void setTestSpringOrderBean(TestSpringOrderBean testSpringOrderBean) {
        System.out.println("启动顺序7:通过注解@Autowired进行依赖的bean注入");
    }

    @Override
    public void setBeanName(@NotNull String beanName) {
        // 设置bean在beanFactory中的名称, 这里的入参beanName就是此bean在bean工厂中已有的名称
        System.out.println("启动顺序8:调用BeanNameAware#setBeanName()方法,进行属性设置");
    }

    @Override
    public void setBeanFactory(@NotNull BeanFactory beanFactory) throws BeansException {
        // 设置bean的beanFactory的引用, 这里的入参beanFactory就是实例化此bean的bean工厂引用
        System.out.println("启动顺序9:调用BeanFactoryAware#setBeanFactory()方法,通过beanFactory实例化bean");
    }

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        System.out.println("启动顺序10:调用ApplicationContextAware#setApplicationContext()方法,设置spring中bean的上下文");
    }

    @PostConstruct
    public void postConstruct() {
        // 实现原理: 通过BeanPostProcessor的前置方法实现
        System.out.println("启动顺序12:通过注解@PostConstruct进行bean属性赋值后处理,当Bean的所有属性被Spring容器注入完成之后调用此时方法");
    }

    @Override
    public void afterPropertiesSet() {
        // 实现原理: 通过InitializingBean接口调用此方法, 此方法在BeanPostProcessor之后执行
        System.out.println("启动顺序13:调用InitializingBean#afterPropertiesSet()方法,当Bean的所有属性被Spring容器注入完成之后调用此方法进行初始化");
    }

    public void customInitMethod() {
        System.out.println("启动顺序14:调用TestSpringOrder#customInitMethod()方法,类指定的初始化方法");
    }

    @Override
    public void afterSingletonsInstantiated() {
        System.out.println("启动顺序16:调用SmartInitializingSingleton#afterSingletonsInstantiated()方法,所有单例bean初始化完成后进行回调");
    }

    private boolean runFlag = false;

    @Override
    public boolean isRunning() {
        System.out.println("启动顺序17和23:调用Lifecycle#isRunning()方法,判断bean状态");
        // 这里的返回值是表示当前bean是否开启, 值的返回会影响org.hf.application.javabase.spring.sequence.TestSpringOrder.start()
        // 和org.hf.application.javabase.spring.sequence.TestSpringOrder.stop()方法的调用
        return runFlag;
    }

    @Override
    public void start() {
        runFlag = true;
        System.out.println("启动顺序18:调用Lifecycle#start(),标记bean状态");
    }

    @Override
    public void onApplicationEvent(@NotNull ContextRefreshedEvent refreshEvent) {
        System.out.println("启动顺序19:调用ApplicationListener#onApplicationEvent()方法,进行事件ContextRefreshedEvent的发布监听");
    }

    @Override
    public void run(String... args) {
        System.out.println("启动顺序21:调用CommandLineRunner#run()方法,进行应用启动后的加载");
    }

    @Override
    public void stop() {
        runFlag = false;
        System.out.println("启动顺序24:调用Lifecycle#stop()方法,状态bean标记");
    }

    @PreDestroy
    public void preDestroy() {
        // 注解@PreDestroy标识的方法,在bean销毁之前执行的业务逻辑
        System.out.println("启动顺序25:通过注解@PreDestroy进行bean销毁前的处理");
    }

    @Override
    public void destroy() throws Exception {
        // 在bean销毁之前执行业务逻辑处理
        System.out.println("启动顺序26:调用DisposableBean#destroy()方法,进行bean销毁前处理");
    }

    public void customDestroyMethod() {
        // 在bean销毁之前执行的业务逻辑
        System.out.println("启动顺序27:调用TestSpringOrder#customDestroyMethod()方法,类指定的bean销毁前处理方法");
    }

}
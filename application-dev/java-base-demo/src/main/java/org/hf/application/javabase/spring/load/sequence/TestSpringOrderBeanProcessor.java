package org.hf.application.javabase.spring.load.sequence;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

/**
 * <p> spring容器启动顺序测试2 </p >
 * 在spring容器初始化时:
 *    1.处理器执行顺序: BeanDefinitionRegistryPostProcessor(BeanDefinition注册前后执行) ->
 *       BeanFactoryPostProcessor(BeanDefinition注册后) ->
 *       InstantiationAwareBeanPostProcessor(bean实例化前后执行) -> BeanPostProcessor(bean初始化前后执行)
 *    2.处理器方法执行顺序:
 *       BeanDefinitionRegistryPostProcessor#postProcessBeanDefinitionRegistry()[bean解析后,BeanDefinition注册前执行] ->
 *       BeanDefinitionRegistryPostProcessor#postProcessBeanFactory()[从父类中继承过来,BeanDefinition注册后,bean实例化前执行] ->
 *       BeanFactoryPostProcessor#postProcessBeanFactory()[BeanDefinition注册后,bean实例化前执行] ->
 *       InstantiationAwareBeanPostProcessor#postProcessBeforeInstantiation()[BeanDefinition注册后,bean实例化前执行] ->
 *       InstantiationAwareBeanPostProcessor#postProcessAfterInstantiation()[bean实例化后,属性赋值前执行] ->
 *       InstantiationAwareBeanPostProcessor#postProcessProperties()[bean实例化后,属性赋值前执行] ->
 *       BeanPostProcessor#postProcessBeforeInitialization()[bean属性赋值后,初始化前执行] ->
 *       BeanPostProcessor#postProcessAfterInitialization()[bean初始化后执行]
 */
@Component
public class TestSpringOrderBeanProcessor implements InstantiationAwareBeanPostProcessor,
        BeanPostProcessor,
        BeanFactoryPostProcessor {

   /**
    * BeanFactoryPostProcessor 是当 BeanDefinition 读取完元数据（也就是从任意资源中定义的 bean 数据）后还未实例化之前可以进行修改
    * @param beanFactory the bean factory used by the application context
    * @throws BeansException 异常
    */
   @Override
   public void postProcessBeanFactory(@NotNull ConfigurableListableBeanFactory beanFactory) throws BeansException {
       // 此时spring还未对bean进行实例化, 可以在这里对指定bean做修改, 就会影响到对应bean的创建
       System.out.println("启动顺序2:调用BeanFactoryPostProcessor#postProcessBeanFactory()方法");
   }

    /**
     * 在Bean创建前(实例化之前)调用，如果返回null，一切按照正常顺序执行，如果返回的是一个实例的对象(返回的是一个代理类)，那么这个将会跳过实例化、
     * 初始化的过程(后续只会调用 postProcessAfterInitialization() 方法)
     * @param beanClass the class of the bean to be instantiated
     * @param beanName the name of the bean
     * @return null-接着执行实例化及之后流程,否则返回代理类对象
     * @throws BeansException 异常
     */
    @Override
    public Object postProcessBeforeInstantiation(@NotNull Class<?> beanClass, @NotNull String beanName) throws BeansException {
        if (isMatchClass(beanClass)) {
            System.out.println("启动顺序3:调用InstantiationAwareBeanPostProcessor#postProcessBeforeInstantiation()方法");
        }
        return null;
    }

    /**
     * 在Bean创建后(实例化之后)调用，可以对已实例化的Bean进行进一步的自定义处理(返回值会影响 postProcessProperties()是否执行，其中返回false
     * 的话，是不会执行)。
     * @param bean the bean instance created, with properties not having been set yet
     * @param beanName the name of the bean
     * @return true-执行InstantiationAwareBeanPostProcessor#postProcessProperties()方法
     * @throws BeansException 异常
     */
    @Override
    public boolean postProcessAfterInstantiation(@NotNull Object bean, @NotNull String beanName) throws BeansException {
        if (isMatchClass(bean.getClass())) {
            System.out.println("启动顺序5:调用InstantiationAwareBeanPostProcessor#postProcessAfterInstantiation()方法");
        }
        return true;
    }

    /**
     * 在Bean设置属性(属性注入)前调用，可以修改Bean的属性值或进行其他自定义操作，当InstantiationAwareBeanPostProcessor
     * #postProcessAfterInstantiation()返回true时才执行(用于修改bean的属性，如果返回值不为空，那么会更改指定字段的值)。
     * @param pvs the property values that the factory is about to apply (never {@code null})
     * @param bean the bean instance created, but whose properties have not yet been set
     * @param beanName the name of the bean
     * @return 修改后的bean属性对象
     * @throws BeansException 异常
     */
    @Override
    public PropertyValues postProcessProperties(@NotNull PropertyValues pvs, @NotNull Object bean, @NotNull String beanName) throws BeansException {
        if (isMatchClass(bean.getClass())) {
            System.out.println("启动顺序6:调用InstantiationAwareBeanPostProcessor#postProcessProperties()方法");
        }
        return pvs;
    }

    /**
     * BeanPostProcessor接口也叫Bean后置处理器，作用是在Bean对象实例化和依赖注入完成后，在配置文件bean的init-method(初始化)方法或者
     * InitializingBean的afterPropertiesSet方法之前添加我们自己的处理逻辑。注意是Bean实例化完毕后及依赖注入完成后触发的
     * @param bean the new bean instance
     * @param beanName the name of the bean
     * @return 处理后的bean对象
     * @throws BeansException 异常
     */
    @Override
    public Object postProcessBeforeInitialization(@NotNull Object bean, @NotNull String beanName) throws BeansException {
        // 执行到这里时bean还没有进行初始化
        if (isMatchClass(bean.getClass())) {
            System.out.println("启动顺序11:调用BeanPostProcessor#postProcessBeforeInitialization()方法,beanName:" + beanName);
        }
        // 这里如果返回null, 则返回原对象, 而不是null
        // return null;
        return bean;
    }

    /**
     * BeanPostProcessor接口也叫Bean后置处理器，作用是在Bean对象实例化和依赖注入完成后，在配置文件bean的init-method(初始化)方法或者
     * InitializingBean的afterPropertiesSet方法之后添加我们自己的处理逻辑。注意是Bean实例化完毕后及依赖注入完成后触发的
     * @param bean the new bean instance
     * @param beanName the name of the bean
     * @return 处理后的bean对象
     * @throws BeansException 异常
     */
    @Override
    public Object postProcessAfterInitialization(@NotNull Object bean, @NotNull String beanName) throws BeansException {
        // 执行到这里时bean已经初始化完成
        if (isMatchClass(bean.getClass())) {
            System.out.println("启动顺序15:调用BeanPostProcessor#postProcessAfterInitialization()方法,beanName:{}" + beanName);
        }
        // 这里如果返回null, 则返回原对象, 而不是null
        // return null;
        return bean;
    }

    private boolean isMatchClass(Class<?> beanClass){
        // 这里只对指定的一个bean进行判断,然后输出前后的调用链路,否则多了会引起输出结果的混乱(会根据不同bean实例,初始化过程打印多次)
        return TestSpringOrder.class.equals(ClassUtils.getUserClass(beanClass));
    }
}
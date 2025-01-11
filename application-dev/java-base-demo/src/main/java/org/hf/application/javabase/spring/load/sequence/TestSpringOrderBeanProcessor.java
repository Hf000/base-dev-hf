package org.hf.application.javabase.spring.load.sequence;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * <p> spring容器启动顺序测试2 </p >
 * 在spring容器初始化时:
 *    1.处理器执行顺序: BeanDefinitionRegistryPostProcessor -> BeanFactoryPostProcessor -> BeanPostProcessor
 *    2.处理器方法执行顺序: BeanDefinitionRegistryPostProcessor#postProcessBeanDefinitionRegistry() ->
 *    BeanDefinitionRegistryPostProcessor#postProcessBeanFactory()[从父类中继承过来] ->
 *    BeanFactoryPostProcessor#postProcessBeanFactory() -> BeanPostProcessor#postProcessBeforeInitialization() ->
 *    BeanPostProcessor#postProcessAfterInitialization()
 */
@Component
public class TestSpringOrderBeanProcessor implements
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
       System.out.println("启动顺序2:BeanFactoryPostProcessor postProcessBeanFactory");
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
        if (bean instanceof TestSpringOrder) {
            System.out.println("启动顺序9:BeanPostProcessor postProcessBeforeInitialization beanName:" + beanName);
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
        if (bean instanceof TestSpringOrder) {
            System.out.println("启动顺序13:BeanPostProcessor postProcessAfterInitialization beanName:{}" + beanName);
        }
        // 这里如果返回null, 则返回原对象, 而不是null
        // return null;
        return bean;
    }
}
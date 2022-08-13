package org.hf.common.config.replacebean;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.ClassScanner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hf.common.config.constants.ConfigCommonConstant;
import org.hf.common.publi.utils.PropertiesUtil;
import org.reflections.Reflections;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hf.common.config.constants.ConfigCommonConstant.ASTERISK;
import static org.hf.common.config.constants.ConfigCommonConstant.EN_COMMA;
import static org.hf.common.config.constants.ConfigCommonConstant.EN_PERIOD;

/**
 * <p> 实现spring容器中的bean替换处理 </p>
 * 自定义spring容器中替换bean注解CustomBeanReplace实现 - 2
 * @author hufei
 * @version 1.0.0
 * @date 2021/10/10 13:16
 */
@Slf4j
@Component
public class CustomBeanFactoryPostProcessor implements BeanDefinitionRegistryPostProcessor {

    @Value("${replaceBean.scannerPackages}")
    @NestedConfigurationProperty
    private String scannerPackages;

    /**
     * bean注册时处理  这种方式替换不需要将替换的实现交给spring管理
     * @param beanDefinitionRegistry bean注册对象
     * @throws BeansException 异常
     */
    @Override
    public void postProcessBeanDefinitionRegistry(@NonNull BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        // 获取指定注解的类
        Set<Class<?>> annotationClass = getAnnotationClass();
        if (CollectionUtil.isNotEmpty(annotationClass)) {
            annotationClass.forEach(aClass -> {
                String replacedBeanName = getTargetClassBeanName(aClass);
                setRegistryBeanReplace(beanDefinitionRegistry, replacedBeanName, aClass);
            });
        }
    }

    /**
     * 设置需要替换的类, 重新注册实例
     * @param beanDefinitionRegistry bean注册对象
     * @param replacedBeanName 被替换的bean名称
     * @param replaceBeanClass 替换的类class
     */
    private void setRegistryBeanReplace(BeanDefinitionRegistry beanDefinitionRegistry, String replacedBeanName, Class<?> replaceBeanClass) {
        if (StringUtils.isBlank(replacedBeanName) || null == replaceBeanClass) {
            return;
        }
        // 先移除原来的bean定义
        beanDefinitionRegistry.removeBeanDefinition(replacedBeanName);
        // 注册自己的bean定义
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(replaceBeanClass);
        // 如果有构造函数参数, 进行以下设置,有几个设置几个, 没有就不用设置
//        beanDefinitionBuilder.addConstructorArgValue("constructorArgValue");
        // 设置init方法, 没有就不设置
//        beanDefinitionBuilder.setInitMethodName("initMethodName");
        // 设置destroy方法, 没有就不设置
//        beanDefinitionBuilder.setDestroyMethodName("destroyMethodName");
        // 将bean的定义注册到spring环境
        beanDefinitionRegistry.registerBeanDefinition(replacedBeanName, beanDefinitionBuilder.getBeanDefinition());
    }

    /**
     * 获取需要被替换的bean名称
     * @param clazz 类class
     * @return beanName
     */
    private String getTargetClassBeanName(Class<?> clazz) {
        String replacedBeanName = null;
        try {
            if (Objects.nonNull(clazz)) {
                CustomBeanReplace replaceBeanName = clazz.getDeclaredAnnotation(CustomBeanReplace.class);
                replacedBeanName = replaceBeanName == null ? null : replaceBeanName.value();
            }
        } catch (Exception e) {
            log.error("ReplaceBean Error", e);
        }
        return replacedBeanName;
    }

    /**
     * 获取指定注解的类
     * @return 返回类
     */
    private Set<Class<?>> getAnnotationClass() {
        try {
            Reflections reflections;
            if (StringUtils.isNotBlank(scannerPackages) && !StringUtils.containsIgnoreCase(scannerPackages, ConfigCommonConstant.NULL_STRING)) {
                // 扫描指定路径下的包
                if (scannerPackages.contains(EN_PERIOD + ASTERISK) && scannerPackages.contains(ASTERISK + EN_PERIOD)) {
                    // 带*号包路径处理
                    String packageStart = StringUtils.substringBefore(scannerPackages, EN_PERIOD + ASTERISK);
                    String packageLast = StringUtils.substringAfterLast(scannerPackages, ASTERISK + EN_PERIOD);
                    Set<Class<?>> classes = ClassScanner.scanPackage(packageStart);
                    return classes.stream().filter(clazz -> clazz.getName().startsWith(packageStart) && clazz.getName().contains(packageLast) && clazz.isAnnotationPresent(CustomBeanReplace.class)).collect(Collectors.toSet());
                } else {
                    String[] split = scannerPackages.split(EN_COMMA);
                    reflections = new Reflections((Object) split);
                }
            } else {
                // 扫所有的包
                reflections = new Reflections();
            }
            // 返回指定注解的所有类对象
            return reflections.getTypesAnnotatedWith(CustomBeanReplace.class);
        } catch (Exception e) {
            log.error("scannerPackages parse error!", e);
        }
        return null;
    }

    /**
     * bean注册后处理 在上面的postProcessBeanDefinitionRegistry方法已经做了bean替换, 这里不需要再次替换, 这里只是代码展示,如果此时替换的做法
     * 这里替换时, 替换和被替换的bean都需要交给spring管理, 并不能同名
     * @param configurableListableBeanFactory bean工厂
     * @throws BeansException 异常
     */
    @Override
    public void postProcessBeanFactory(@NonNull ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        /*DefaultListableBeanFactory defaultListableBeanFactory;
        if (configurableListableBeanFactory instanceof DefaultListableBeanFactory) {
            defaultListableBeanFactory = (DefaultListableBeanFactory) configurableListableBeanFactory;
            for (String beanDefinitionName : defaultListableBeanFactory.getBeanDefinitionNames()) {
                String beanClassName = defaultListableBeanFactory.getBeanDefinition(beanDefinitionName).getBeanClassName();
                try {
                    if (StringUtils.isBlank(beanClassName)) {
                        log.warn("cannot find replaceBeanName: " + beanDefinitionName + ", replaceClassName" + beanClassName);
                        continue;
                    }
                    Class<?> aClass = Class.forName(beanClassName, false, Thread.currentThread().getContextClassLoader());
                    String replacedBeanName = getTargetClassBeanName(aClass);
                    if (Objects.nonNull(replacedBeanName)) {
                        log.info("replace replaceBean: " + beanDefinitionName + ": " + beanClassName + ", replacedBeanName:" + replacedBeanName);
                        // 将要替换的实现先移除    这里不能移除被替换的bean, 否则下面注册会报错
                        if (defaultListableBeanFactory.containsBeanDefinition(beanDefinitionName)) {
                            defaultListableBeanFactory.removeBeanDefinition(beanDefinitionName);
                        }
                        // 注册新的bean定义和实例   然后将要替换的bean的实现重新注册,覆盖掉原有的实现    这种方式需要配置spring覆盖bean配置项为true:spring.main.allow-bean-definition-overriding=true
                        defaultListableBeanFactory.registerBeanDefinition(replacedBeanName, BeanDefinitionBuilder.genericBeanDefinition(beanClassName).getBeanDefinition());
                    }
                } catch (ClassNotFoundException e) {
                    log.error("classNotFound ===> replaceBean =" + beanDefinitionName + ", " + beanClassName, e);
                } catch (Exception e) {
                    log.error("替换bean异常", e);
                }
            }
        }*/
    }
}

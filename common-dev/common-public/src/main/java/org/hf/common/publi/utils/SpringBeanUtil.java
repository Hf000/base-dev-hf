package org.hf.common.publi.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * <p> 获取Spring容器中的Bean工具类 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2021/10/10 16:39
 */
@Component("customSpringBeanUtil")
public class SpringBeanUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    /**
     * 设置spring上下文对象
     * @param applicationContext spring上下文对象
     * @throws BeansException 异常
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (null == SpringBeanUtil.applicationContext) {
            SpringBeanUtil.applicationContext = applicationContext;
        }
    }

    /**
     * 获取Spring上下文对象
     * @return 返回
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 通过name获取bean对象
     * @param beanName bean名称
     * @return bean对象
     */
    public static Object getBean(String beanName) {
        if (StringUtils.isBlank(beanName)) {
            return null;
        }
        return getApplicationContext().getBean(beanName);
    }

    /**
     * 通过class获取bean对象
     * @param clazz bean的class
     * @param <T> 类型
     * @return bean对象
     */
    public static <T> T getBean(Class<T> clazz) {
        if (null == clazz) {
            return null;
        }
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 通过bean名称和class获取指定的bean对象
     * @param beanName bean名称
     * @param clazz bean的class
     * @param <T> 类型
     * @return bean对象
     */
    public static <T> T getBean(String beanName, Class<T> clazz) {
        if (StringUtils.isBlank(beanName) && null == clazz) {
            return null;
        }
        return getApplicationContext().getBean(beanName, clazz);
    }

    /**
     * 通过接口class获取所有子实现bean对象
     * @param clazz 接口class
     * @param <T> 类型
     * @return bean对象集合
     */
    public static <T> Map<String, T> getBeanListByInterface(Class<T> clazz) {
        if (null == clazz) {
            return null;
        }
        return getApplicationContext().getBeansOfType(clazz);
    }

}

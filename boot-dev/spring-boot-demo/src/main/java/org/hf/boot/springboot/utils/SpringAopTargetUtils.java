package org.hf.boot.springboot.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.support.AopUtils;
import org.springframework.aop.target.SingletonTargetSource;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Field;

/**
 * 获取spring中代理类对象基础类型工具类,spring中除了{@link AopUtils}工具类,还有{@link ClassUtils}(ClassUtils#getUserClass()获取用
 *  户定义的类)工具类,获取类相关信息
 * 注意: 有基础类型的对象才能获取到基础类型对象,注意以下场景
 *  1.可以获取到:例如常见的service接口及其impl实现交给spring管理bean,在方法上有@Transactional注解时,通过以下方法就可以获取到基础类型对象
 *  2.不可以获取到:例如FeignClient,因为其进行动态代理时,没有接收需要代理的基础类对象,只是根据接口动态生成了一个代理类对象,所以获取不到基础类型
 */
@Slf4j
public class SpringAopTargetUtils {

    /** 
     * 获取代理对象的目标对象
     * @param proxy 代理对象 
     * @return 目标对象
     */
    public static Object getTarget(Object proxy) {
        if(!AopUtils.isAopProxy(proxy)) {
            // 不是springProxy类型代理对象
            return proxy;
        }
        try {
            if(AopUtils.isJdkDynamicProxy(proxy)) {
                // 判断如果是jdk动态代理
                return getJdkDynamicProxyTargetObject(proxy);
            } else {
                //cglib
                return getCglibProxyTargetObject(proxy);
            }
        } catch (Exception e) {
            log.error("获取代理对象目标对象异常", e);
        }
        return proxy;
    }
  
    private static Object getCglibProxyTargetObject(Object proxy) throws Exception {  
        Field h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
        h.setAccessible(true);  
        Object dynamicAdvisedInterceptor = h.get(proxy);  
        Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
        advised.setAccessible(true);  
        return ((AdvisedSupport)advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();
    }
  
  
    private static Object getJdkDynamicProxyTargetObject(Object proxy) throws Exception {  
        Field h = proxy.getClass().getSuperclass().getDeclaredField("h");  
        h.setAccessible(true);  
        AopProxy aopProxy = (AopProxy) h.get(proxy);
        Field advised = aopProxy.getClass().getDeclaredField("advised");
        advised.setAccessible(true);  
        return ((AdvisedSupport)advised.get(aopProxy)).getTargetSource().getTarget();
    }

    /**
     * 获取代理类对象的基础类型
     * 如果Spring的版本为4.3.8或以上 直接调用AopProxyUtils.getSingletonTarget(target)就可以获取target对象了
     * @param proxy 代理对象
     * @return 基础类型
     */
    public static Class<?> getTargetClass(Object proxy) {
        // 循环获取出多次代理后的原始类型
        while (proxy instanceof Advised) {
            TargetSource targetSource = ((Advised) proxy).getTargetSource();
            if (targetSource instanceof SingletonTargetSource) {
                proxy = ((SingletonTargetSource) targetSource).getTarget();
            }
        }
        // 获取代理的基础类型
        if (proxy == null) {
            return null;
        }
        return AopUtils.getTargetClass(proxy);
    }
} 
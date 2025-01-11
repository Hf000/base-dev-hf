package org.hf.boot.springboot.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.support.AopUtils;

import java.lang.reflect.Field;

/**
 * Aop工具类
 * @author HF
 */
@Slf4j
public class AopTargetUtils {  
  
      
    /** 
     * 获取代理对象的目标对象
     * @param proxy 代理对象 
     * @return 目标对象
     */
    public static Object getTarget(Object proxy) {
        if(!AopUtils.isAopProxy(proxy)) {
            //不是代理对象
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
      
} 
package org.hf.application.custom.framework.framework.factory;

/**
 * <p> bean实例工厂 </p>
 *
 * @author hufei
 * @date 2022/7/17 19:24
 */
public interface BeanFactory {

    /**
     * 1、根据uri获取实例
     * @param uri 请求uri入参
     * @return Object -> uri对应实例bean对象
     */
    Object getUrlBean(String uri);

    /**
     * 2、根据id获取实例bean对象
     * @param id  bean对象id标识
     * @return Object -> id对应的实例bean对象
     */
    Object getBean(String id);
}

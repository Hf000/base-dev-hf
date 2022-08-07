package org.hf.application.custom.framework.framework.factory;

/**
 * <p>  </p>
 * @author hufei
 * @date 2022/7/17 19:24
*/
public interface BeanFactory {


    /***
     * 1、根据uri获取实例
     */
    Object getUrlBean(String uri);


    /***
     * 2、根据id获取实例
     */
    Object getBean(String id);
}

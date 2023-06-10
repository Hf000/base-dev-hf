package org.hf.boot.springboot.proxy.impl.controller;

import org.hf.boot.springboot.proxy.impl.servlet.CustomServiceProxyServlet;
import org.hf.boot.springboot.proxy.mvc.ProxyServletController;

/**
 * <p> 自定义服务代理servletController </p >
 * 继承ProxyServletController 创建 CustomServiceProxyServletController，实现带有CustomServiceProxyServlet的controller
 * 自定义服务代理实现 - 2
 * 业务根据具体情况实现ProxyServletController，初始化设置自定义代理servlet实现CustomServiceProxyServlet类，并实例化bean
 * @author HUFEI
 * @date 2023-06-05
 **/
public class CustomServiceProxyServletController extends ProxyServletController {

    public CustomServiceProxyServletController() {
        setServletClass(CustomServiceProxyServlet.class);
    }
}
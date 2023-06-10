package org.hf.boot.springboot.proxy.mvc;

import org.hf.boot.springboot.proxy.servlet.AbstractProxyServlet;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ServletWrappingController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p> 代理servlet controller </p >
 * 请求代理器实现 - 5
 * 继承ServletWrappingController实现ProxyServletController，设置AbstractProxyServlet，重写handleRequestInternal设置ModelAndView;
 * ProxyServletController初始化流程: ProxyServletController初始化 -> 设置并初始化AbstractProxyServlet -> AbstractProxyServlet获取路由信息,维护路由映射 -> ProxyServletController注册到Spring容器
 * @author HUFEI
 * @date 2023-06-05
 **/
public class ProxyServletController extends ServletWrappingController {

    public ProxyServletController(){
        setServletClass(AbstractProxyServlet.class);
    }

    @Override
    protected ModelAndView handleRequestInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response) throws Exception {
        return super.handleRequestInternal(request,response);
    }
}
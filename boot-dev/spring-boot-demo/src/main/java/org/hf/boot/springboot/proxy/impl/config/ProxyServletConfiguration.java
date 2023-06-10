package org.hf.boot.springboot.proxy.impl.config;

import lombok.extern.slf4j.Slf4j;
import org.hf.boot.springboot.interceptor.CustomizeInterceptor;
import org.hf.boot.springboot.proxy.handler.ProxyServletHandlerMapping;
import org.hf.boot.springboot.proxy.impl.controller.CustomServiceProxyServletController;
import org.hf.boot.springboot.proxy.properties.ProxyServletProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * <p> 自定义代理servlet配置类 </p >
 * 新增配置ProxyServletConfiguration，初始化新增的CustomServiceProxyServletController，并加入ProxyServletHandlerMapping，可根据具体需要添加拦截器
 * 自定义服务代理实现 - 3
 * 实例化ProxyServletHandlerMapping和ProxyServletController
 * @author HUFEI
 * @date 2023-06-05
 **/
@Slf4j
@Configuration
@EnableConfigurationProperties(ProxyServletProperties.class)
@ConditionalOnProperty(name = "proxy.servlet.open", havingValue = "true")
public class ProxyServletConfiguration {

    @Autowired
    private ProxyServletProperties proxyServletProperties;

    @Lazy
    @Autowired
    public CustomizeInterceptor customizeInterceptor;

    @Bean
    public CustomServiceProxyServletController proxyServletController() {
        return new CustomServiceProxyServletController();
    }

    @Bean
    public ProxyServletHandlerMapping proxyServletHandlerMapping() {
        ProxyServletHandlerMapping handlerMapping = new ProxyServletHandlerMapping(proxyServletProperties);
        // 如果这里有请求拦截器需要将拦截器注册添加到处理器映射器, 不然会拦截不到
        handlerMapping.addInterceptor(customizeInterceptor);
        return handlerMapping;
    }
}
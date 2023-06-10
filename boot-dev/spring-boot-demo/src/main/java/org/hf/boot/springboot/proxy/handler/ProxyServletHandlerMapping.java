package org.hf.boot.springboot.proxy.handler;

import org.apache.commons.collections4.CollectionUtils;
import org.hf.boot.springboot.proxy.properties.ProxyServletProperties;
import org.hf.boot.springboot.utils.SpringContextUtil;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.AbstractUrlHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p> proxy servlet handlerMapping 自定义扩展代理处理映射器 </p >
 * 请求代理器实现 - 2
 * 继承AbstractUrlHandlerMapping实现ProxyServletHandlerMapping类，根据ProxyServletProperties信息调用原生registerHandler方法维护urlPath-Controller映射，同时重写buildPathExposingHandler提供HandlerInterceptor维护功能；
 * ProxyServletHandlerMapping初始化流程：设置ProxyServletHandlerMapping(添加路由配置和添加HandlerInterceptor) -> 根据路由配置注册handlers(设置url和controller映射关系) -> handlerInterceptor添加到handlerExecutionChain -> 调整ProxyServletHandlerMapping优先级(比RequestMappingHandlerMapping高) -> 注册ProxyServletHandlerMapping到Spring容器
 * @author HUFEI
 * @date 2023-06-05
 **/
public class ProxyServletHandlerMapping extends AbstractUrlHandlerMapping {

    private final ProxyServletProperties proxyServletProperties;

    private final List<HandlerInterceptor> handlerInterceptorList = new ArrayList<>();


    public ProxyServletHandlerMapping(ProxyServletProperties proxyServletProperties) {
        this.proxyServletProperties = proxyServletProperties;
        setOrder(-10);
        //注册handler
        registerHandlers();
    }

    @Override
    protected Object lookupHandler(@NonNull String urlPath, @NonNull HttpServletRequest request) throws Exception {
        return super.lookupHandler(urlPath, request);
    }

    @Override
    @NonNull
    protected Object buildPathExposingHandler(@NonNull Object rawHandler, @NonNull String bestMatchingPattern, @NonNull String pathWithinMapping, @Nullable Map<String, String> uriTemplateVariables) {
        HandlerExecutionChain chain = (HandlerExecutionChain) super.buildPathExposingHandler(rawHandler, bestMatchingPattern, pathWithinMapping, uriTemplateVariables);
        if (CollectionUtils.isNotEmpty(handlerInterceptorList)) {
            for (HandlerInterceptor handlerInterceptor : handlerInterceptorList) {
                chain.addInterceptor(handlerInterceptor);
            }
        }
        return chain;
    }

    public void addInterceptor(HandlerInterceptor handlerInterceptor) {
        handlerInterceptorList.add(handlerInterceptor);
    }

    private void registerHandlers() {
        List<ProxyServletProperties.ProxyServletServerProperties> servers = proxyServletProperties.getServer();
        if (CollectionUtils.isNotEmpty(servers)) {
            for (ProxyServletProperties.ProxyServletServerProperties server : servers) {
                Object controlBean;
                try {
                    controlBean = SpringContextUtil.getBean(Class.forName(server.getProxyClass()));
                } catch (Exception e) {
                    throw new IllegalThreadStateException("初始化失败，查询不到类" + server.getProxyClass());
                }
                for (ProxyServletProperties.ProxyServletLocationProperties locationProperties : server.getLocation()) {
                    String urlPath = server.getUrlPattern() + locationProperties.getPath();
                    registerHandler(urlPath, controlBean);
                }
            }
        }
    }
}
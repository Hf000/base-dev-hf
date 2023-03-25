package org.hf.application.custom.shop.config;

import org.hf.application.custom.shop.interceptor.AuthorizationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <p> mvc配置 </p>
 *
 * @author hufei
 * @date 2022/7/17 19:55
 */
@Component
public class MvcConfig implements WebMvcConfigurer {

    @Autowired
    private AuthorizationInterceptor authorizationInterceptor;

    /**
     * 拦截器配置
     * @param registry 注册对象
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorizationInterceptor).
                addPathPatterns("/**").
                excludePathPatterns("/user/login", "/file/upload");
    }
}

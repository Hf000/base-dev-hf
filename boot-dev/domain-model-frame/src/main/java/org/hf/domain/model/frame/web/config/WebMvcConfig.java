package org.hf.domain.model.frame.web.config;

import org.hf.domain.model.frame.web.interceptor.SwaggerInterceptor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <p> web的配置类 </p >
 *
 * @author hufei
 * @date 2022/8/12 23:06
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired(required = false)
    public SwaggerInterceptor swaggerInterceptor;

    /**
     * 添加自定义拦截器到springMVC的拦截器链中
     * @param registry 拦截器注册对象
     */
    @Override
    public void addInterceptors(@NotNull InterceptorRegistry registry) {
        /* addPathPatterns 用于添加拦截规则,/**表示拦截所有请求 */
        if (swaggerInterceptor != null) {
            registry.addInterceptor(swaggerInterceptor)
                    // 拦截所有的路径
                    .addPathPatterns("/*");
            // 放行swagger相关路径
//                .excludePathPatterns("/**/swagger-ui/**")
//                .excludePathPatterns("/**/swagger-resources/**")
//                .excludePathPatterns("/**/v3/**");
        }
    }
}
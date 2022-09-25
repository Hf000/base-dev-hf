package org.hf.springboot.web.config;

import org.hf.springboot.web.interceptor.RequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <p> web的配置类 </p >
 *
 * @author hufei
 * @date 2022/8/12 23:06
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 自定义拦截器
     */
    @Lazy
    @Autowired
    public RequestInterceptor requestInterceptor;

    /**
     * 添加自定义拦截器到springMVC的拦截器链中
     *
     * @param registry 拦截器注册对象
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestInterceptor)
                // 拦截所有的路径
                .addPathPatterns("/*");
    }

}
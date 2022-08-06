package org.hf.boot.springboot.config;

import org.hf.boot.springboot.interceptor.CustomizeInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author:hufei
 * @CreateTime:2020-09-09
 * @Description:mvc的配置类
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    //注册拦截器到springboot
    @Bean
    public CustomizeInterceptor customizeInterceptor() {
        return new CustomizeInterceptor();
    }

    //添加自定义拦截器到springMVC的拦截器链
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(customizeInterceptor()).addPathPatterns("/*");//拦截所有的
    }

}

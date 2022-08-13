package org.hf.boot.springboot.config;

import org.hf.boot.springboot.filter.CustomFilter;
import org.hf.boot.springboot.interceptor.CustomizeInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;

/**
 * <p> web的配置类 </p>
 * @author hufei
 * @date 2022/8/12 23:06
*/
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 自定义拦截器对象初始化
     * @return 自定义拦截器对象
     */
    @Bean
    public CustomizeInterceptor initCustomizeInterceptor() {
        return new CustomizeInterceptor();
    }

    /**
     * 添加自定义拦截器到springMVC的拦截器链中
     * @param registry 拦截器注册对象
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(initCustomizeInterceptor())
                // 拦截所有的路径
                .addPathPatterns("/*");
    }

    /**
     * 配置自定义过滤器方式二
     * 初始化自定义过滤器对象
     * @return 自定义过滤器对象
     */
    /*@Bean
    public CustomFilter initCustomFilter() {
        return new CustomFilter();
    }*/

    /**
     * 配置自定义过滤器方式三
     * //@ConditionalOnProperty(prefix = "request", value = "filter.enable", havingValue = "true")  // bean初始化条件
     * @return 过滤器注册Bean对象
     */
    @Bean
    @ConditionalOnProperty(prefix = "request", value = "filter.enable", havingValue = "true")
    public FilterRegistrationBean<Filter> setFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        // 配置自定义拦截器
        filterRegistrationBean.setFilter(new CustomFilter());
        // 拦截所有路径
        filterRegistrationBean.addUrlPatterns("/*");
        // 设置过滤器执行顺序, 从小到大执行
//        filterRegistrationBean.setOrder(Integer.MAX_VALUE);
        return filterRegistrationBean;
    }

}

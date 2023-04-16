package org.hf.springboot.web.config;

import com.google.common.collect.Maps;
import org.hf.springboot.web.filter.XssFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.Map;

/**
 * <p> XSS过滤配置 </p >
 * @author hfuei
 * @date 2023-04-10
 **/
@Configuration
public class XssConfig {

    /**
     * 注册XSS过滤器
     * @return FilterRegistrationBean<Filter>
     */
    @Bean
    public FilterRegistrationBean<Filter> xssFilterRegistrationBean() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new XssFilter());
        // 设置过滤器执行顺序, 从小到大执行
        filterRegistrationBean.setOrder(1);
        // 设置过滤器是否开启
        filterRegistrationBean.setEnabled(true);
        // 设置过滤器需要过滤的路径
        filterRegistrationBean.addUrlPatterns("/*/testfsfs/*");
        // 设置初始化参数
        Map<String, String> initParameters = Maps.newHashMap();
        initParameters.put("excludes", "/*/wl/sf/routePush,/*/wl/sf/orderStatePush,/*/wl/sf/wpStatePush,/*/msg/send/email/send,/*/base/msg/sms/getShortLink");
        initParameters.put("isIncludeRichText", "true");
        filterRegistrationBean.setInitParameters(initParameters);
        return filterRegistrationBean;
    }
}
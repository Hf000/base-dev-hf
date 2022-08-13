package org.hf.boot.springboot.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * <p> 自定义过滤器 </p>
 * 1. 配置自定义过滤器方式一
 * //@ConditionalOnProperty(prefix = "request", value = "filter.enable", havingValue = "true")   // 配置该类是否能够实例化的条件, 和@Configuration配合使用
 * //@Configuration  // 将该类注册到spring容器中, 配置自定义过滤器方式一, 这样就不需要另外配置org.hf.boot.springboot.config.WebConfig中的initCustomFilter()或者setFilter()方法了, 缺点是不能配置自定义拦截器参数
 * 2. 配置自定义过滤器方式二、三在org.hf.boot.springboot.config.WebConfig类中
 * 3. 配置自定义过滤器方式四
 * //@WebFilter  // 需要在启动类上开启扫描注解@ServletComponentScan才能生效
 * @author hufei
 * @version 1.0.0
 * @date 2022/8/12 22:50
 */
@Slf4j
public class CustomFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 如果没有逻辑可不重写
        log.info("自定义过滤器初始化方法");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("自定义过滤器执行方法");
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        filterChain.doFilter(request, servletResponse);
    }

    @Override
    public void destroy() {
        // 如果没有逻辑可不重写
        log.info("自定义过滤器销毁方法");
    }
}

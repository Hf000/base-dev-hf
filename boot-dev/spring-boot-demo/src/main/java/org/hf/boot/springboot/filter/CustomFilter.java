package org.hf.boot.springboot.filter;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hf.boot.springboot.utils.SpringContextUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * <p> 自定义过滤器 </p>
 * 1. 配置自定义过滤器方式一
 * //@ConditionalOnProperty(prefix = "request", value = "filter.enable", havingValue = "true")   // 配置该类是否能够实例化
 *  的条件, 和@Configuration配合使用
 * //@Configuration  // 将该类注册到spring容器中, 配置自定义过滤器方式一, 这样就不需要另外配置org.hf.boot.springboot.config
 *  .WebConfig中的initCustomFilter()或者setFilter()方法了, 缺点是不能配置自定义拦截器参数
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
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if (RequestMethod.GET.name().equals(request.getMethod()) || RequestMethod.DELETE.name().equals(request.getMethod())) {
            log.info("{} RequestFilter Request Uri:{}, Params:{}", request.getMethod(), request.getRequestURI(), SpringContextUtil.readQueryStringParameters(request));
            filterChain.doFilter(request, servletResponse);
        } else if (RequestMethod.POST.name().equals(request.getMethod()) || RequestMethod.PUT.name().equals(request.getMethod())) {
            String contentType = request.getContentType();
            if (StringUtils.isEmpty(contentType)) {
                log.info("{} RequestFilter Uri:{}, ContentType is null", request.getMethod(), request.getRequestURI());
                filterChain.doFilter(request, servletResponse);
            } else if (StringUtils.isNotBlank(contentType) && contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
                RequestWrapper requestWrapper = new RequestWrapper(request);
                String bodyString = requestWrapper.getRequestBody();
                log.info("{} RequestFilter Uri:{}, ContentType:{}, Params:{}", request.getMethod(), request.getRequestURI(), contentType, bodyString);
                filterChain.doFilter(requestWrapper, servletResponse);
            } else {
                log.info("{} RequestFilter Uri:{}, ContentType:{}", request.getMethod(), request.getRequestURI(), request.getContentType());
                filterChain.doFilter(request, servletResponse);
            }
        }
    }

    @Override
    public void destroy() {
        // 如果没有逻辑可不重写
        log.info("自定义过滤器销毁方法");
    }

    /***
     * HttpServletRequest 包装器
     * 解决: request.getInputStream()只能读取一次的问题
     * 目标: 流可重复读
     */
    public static class RequestWrapper extends HttpServletRequestWrapper {
        /**
         * 请求体
         */
        @Setter
        @Getter
        private String requestBody;

        public RequestWrapper(HttpServletRequest request) {
            super(request);
            // 将body数据存储起来
            requestBody = getRequestBody(request);
        }

        /**
         * 获取请求体
         */
        private String getRequestBody(HttpServletRequest request) {
            return SpringContextUtil.getBodyString(request);
        }

        @Override
        public BufferedReader getReader() {
            return new BufferedReader(new InputStreamReader(getInputStream()));
        }

        @Override
        public ServletInputStream getInputStream() {
            // 创建字节数组输入流
            final ByteArrayInputStream bais = new ByteArrayInputStream(requestBody.getBytes(StandardCharsets.UTF_8));
            return new ServletInputStream() {
                @Override
                public boolean isFinished() {
                    return false;
                }

                @Override
                public boolean isReady() {
                    return false;
                }

                @Override
                public void setReadListener(ReadListener readListener) {
                }

                @Override
                public int read() {
                    return bais.read();
                }
            };
        }
    }
}

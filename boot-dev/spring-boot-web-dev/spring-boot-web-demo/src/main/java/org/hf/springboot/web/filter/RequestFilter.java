package org.hf.springboot.web.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hf.common.web.utils.HttpContextUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
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
 * <p> 请求过滤器 </p >
 *
 * @author hufei
 * @date 2022-09-05
 **/
@Slf4j
@Order(Integer.MIN_VALUE)
@Configuration
@ConditionalOnProperty(prefix = "request", value = "filter.enabled", havingValue = "true")
public class RequestFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if (RequestMethod.GET.name().equals(request.getMethod()) || RequestMethod.DELETE.name().equals(request.getMethod())) {
            log.info("{} RequestFilter Request Uri:{}, Params:{}", request.getMethod(), request.getRequestURI(), HttpContextUtils.readQueryStringParameters(request));
            filterChain.doFilter(request, servletResponse);
        } else if (RequestMethod.POST.name().equals(request.getMethod()) || RequestMethod.PUT.name().equals(request.getMethod())) {
            String contentType = request.getContentType();
            if (StringUtils.isNotBlank(contentType) && contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
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
        private final String requestBody;

        public RequestWrapper(HttpServletRequest request) {
            super(request);
            // 将body数据存储起来
            requestBody = getRequestBody(request);
        }

        /**
         * 获取请求体
         */
        private String getRequestBody(HttpServletRequest request) {
            return HttpContextUtils.getBodyString(request);
        }

        /**
         * 获取请求体
         */
        public String getRequestBody() {
            return requestBody;
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
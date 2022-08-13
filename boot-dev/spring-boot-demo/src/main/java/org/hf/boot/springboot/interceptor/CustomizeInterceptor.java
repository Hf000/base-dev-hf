package org.hf.boot.springboot.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p> 自定义拦截器 </p>
 * //实现自定义拦截器，这里完后还要添加一个配置类注册拦截器并添加到springMVC拦截器链中，例如：org.hf.boot.springboot.config.WebConfig配置类
 * @author hufei
 * @date 2022/8/12 23:28
*/
@Slf4j
public class CustomizeInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("这里是自定义拦截器的前置方法，在执行处理器之前执行");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("这里是自定义拦截器的后置方法，在执行处理器之后执行");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("这里是自定义拦截器的完成时方法，在渲染视图解析器之后执行");
    }
}

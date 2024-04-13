package org.hf.domain.model.frame.web.interceptor;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hf.domain.model.frame.web.config.knife4j.Knife4jConfig;
import org.hf.domain.model.frame.web.error.exception.CustomWebException;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义拦截器,用于swagger接口调用模仿用户登录, 自定义Web拦截器需要手动添加到WebMvc配置中,
 * 这里在{@link org.hf.domain.model.frame.web.config.WebMvcConfig}中实现
 */
@Slf4j
@Configuration
@ConditionalOnProperty(name = "spring.profiles.active", havingValue = "dev")
public class SwaggerInterceptor implements HandlerInterceptor {
    
    /**
     * 前置拦截器 在请求达到controller之前调用, 可以打印请URI和表单参数
     */
    @SneakyThrows
    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        // TODO 从当前线程中已有用户信息时跳过
        // 获取swagger用户信息
        String userName = request.getHeader(Knife4jConfig.SWAGGER_UM);
        // 根据userName查询是否存在
        boolean isUser = true;
        if (isUser) {
            // 存在则放行
            return true;
        }
        throw new CustomWebException("查询Swagger免登用户信息异常, UserName=" + userName);
    }

    /**
     * 请求处理之后进行调用,但是在视图被渲染之前（Controller方法调用之后）
     */
    @Override
    public void postHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, ModelAndView modelAndView) {
        // To do nothing.
    }

    /**
     * 后置拦截器 在整个请求结束之后被调用,也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）
     */
    @Override
    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, Exception ex) {
        // To do nothing.
    }
}
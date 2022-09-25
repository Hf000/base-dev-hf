package org.hf.springboot.web.interceptor;

import com.github.pagehelper.page.PageMethod;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hf.common.web.utils.IpUtil;
import org.hf.springboot.service.pojo.bo.UserContext;
import org.hf.springboot.service.pojo.dto.UserInfoDTO;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p> 请求拦截器 </p >
 *
 * @author hufei
 * @date 2022-09-05
 **/
@Slf4j
@Configuration
public class RequestInterceptor implements HandlerInterceptor {

    /**
     * 前置拦截器 在请求达到controller之前调用
     * 打印请URI和表单参数
     */
    @SneakyThrows
    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        // 获取用户信息,放入UserContext
        String userIp = IpUtil.getReqIp(request);
        UserContext.addTime(System.currentTimeMillis());
        // TODO 根据前端传入的token获取用户信息
        UserContext.addUser(new UserInfoDTO());
        return true;
    }

    /**
     * 请求处理之后进行调用,但是在视图被渲染之前（Controller方法调用之后）
     */
    @Override
    public void postHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, ModelAndView modelAndView) {
        // To do nothing.
    }

    /**
     * 后置拦截器 在整个请求结束之后被调用,也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）
     */
    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, Exception ex) {
        // 清除线程中的用户信息
        UserContext.removeUser();
        // 清除线程中的PageHelper分页信息
        PageMethod.clearPage();
    }

}
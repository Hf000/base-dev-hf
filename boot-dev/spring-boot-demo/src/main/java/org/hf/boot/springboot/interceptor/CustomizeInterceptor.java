package org.hf.boot.springboot.interceptor;

import cn.hutool.core.util.NumberUtil;
import lombok.extern.slf4j.Slf4j;
import org.hf.boot.springboot.config.UserContext;
import org.hf.boot.springboot.pojo.dto.UserInfoDTO;
import org.jetbrains.annotations.NotNull;
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

    public static final String REQUEST_HEADER_X_USER_INFO = "X-USER-INFO";
    public static final String REQUEST_HEADER_SYSTEM_CODE = "SYSTEM-CODE";

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        log.info("这里是自定义拦截器的前置方法，在执行处理器之前执行");
        // 获取用户信息,放入UserContext
        String xUserInfoStr = request.getHeader(REQUEST_HEADER_X_USER_INFO);
        String systemCode = request.getHeader(REQUEST_HEADER_SYSTEM_CODE);
        UserContext.addTime(System.currentTimeMillis());
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        if (NumberUtil.isNumber(xUserInfoStr)) {
            userInfoDTO.setUserId(NumberUtil.parseInt(xUserInfoStr));
        }
        userInfoDTO.setSystemCode(systemCode);
        UserContext.addUser(userInfoDTO);
        return true;
    }

    @Override
    public void postHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, ModelAndView modelAndView) throws Exception {
        log.info("这里是自定义拦截器的后置方法，在执行处理器之后执行");
    }

    @Override
    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, Exception ex) throws Exception {
        log.info("这里是自定义拦截器的完成时方法，在渲染视图解析器之后执行");
        // 清除线程中的用户信息
        UserContext.removeUser();
    }
}

package org.hf.application.custom.shop.interceptor;

import org.hf.application.custom.shop.user.SessionShare;
import org.hf.application.custom.shop.user.SessionThreadLocal;
import org.hf.application.custom.shop.util.JwtTokenUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p> 认证拦截器 </p>
 *
 * @author hufei
 * @date 2022/7/17 20:00
 */
@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    @Autowired
    private SessionThreadLocal sessionThreadLocal;

    /**
     * 请求前置处理 -- 将用户会话存储到ThreadLocal中
     * @param request 请求
     * @param response 响应
     * @param handler 处理器
     * @return 是否放行
     * @throws Exception 异常
     */
    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        try {
            //获取令牌
            String authorization = request.getHeader("token");
            //解析令牌
            if (!StringUtils.isEmpty(authorization)) {
                Map<String, Object> tokenMap = JwtTokenUtil.parseToken(authorization);
                //封装用户身份信息，存储到ThreadLocal中，供当前线程共享使用
                //1.封装需要共享的信息
                //2.创建一个对象继承封装信息，每次共享该对象 (不需要共享，则可以创建另外一个对象继承它)
                //3.创建共享管理对象，实现共享信息的增加、获取、移除功能
                SessionShare session = new SessionShare(
                        tokenMap.get("username").toString(),
                        tokenMap.get("name").toString(),
                        tokenMap.get("sex").toString(),
                        tokenMap.get("role").toString(),
                        Integer.valueOf(tokenMap.get("level").toString())
                );
                sessionThreadLocal.add(session);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //输出令牌校验失败
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().print("身份校验失败!");
        response.getWriter().close();
        return false;
    }

    /**
     * 请求后置处理方法 -- 移除会话信息
     * @param request 请求
     * @param response  响应
     * @param handler   处理器
     * @param modelAndView 渲染对象
     */
    @Override
    public void postHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, ModelAndView modelAndView) {
        sessionThreadLocal.remove();
    }
}

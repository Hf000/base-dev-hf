package org.hf.common.web.aop;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.hf.common.publi.utils.TimeUtil;
import org.hf.common.web.contants.WebCommonConstant;
import org.hf.common.web.utils.RequestParamUtil;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * <p> 对controller层请求参数增强处理 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2021/10/9 15:29
 */
@Slf4j
@Component
@Aspect
public class RequestParameterAspect {

    /**
     * 拦截org.hf包及其子包下面所有以Controller结尾的类里面的所有方法
     */
    @Pointcut("execution(public * org.hf..*Controller.*(..))")
    public void controllerMethodPointcut() {
    }

    @Around("controllerMethodPointcut()")
    public Object handleControllerMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取当前请求域对象
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        // 获取当前请求对象
        HttpServletRequest request = requestAttributes.getRequest();
        // 获取当前切面的类全名
        String beanName = joinPoint.getSignature().getDeclaringTypeName();
        // 获取切面方法名称
        String methodName = joinPoint.getSignature().getName();
        // 获取当前请求的相对路径
        String requestUri = request.getRequestURI();
        log.info("RequestParameterAspect===>开始处理【{}】的【{}】方法，请求地址【{}】", beanName, methodName, requestUri);
        // 获取当前请求的指定参数
        String requestId = request.getParameter(WebCommonConstant.REQUEST_ID);
        // 获取方法传参
        Object[] paramsArray = joinPoint.getArgs();
        if (StringUtils.equalsIgnoreCase(HttpMethod.GET.name(), request.getMethod())) {
            // get请求处理
        } else {
            // 其他请求处理
        }
        RequestParamUtil.resetRequestId(requestId);
        // TODO 此方法内可以根据具体的业务场景进行应用, 例如添加接口的凭证校验...
        // 计算当前方法耗时
        TimeUtil timeUtil = new TimeUtil();
        // 执行方法
        Object result = joinPoint.proceed();
        log.info("RequestParameterAspect===>结束处理【{}】的【{}】方法，耗时【{}】", beanName, methodName, timeUtil.getDeltaTimeText());
        return result;
    }

}

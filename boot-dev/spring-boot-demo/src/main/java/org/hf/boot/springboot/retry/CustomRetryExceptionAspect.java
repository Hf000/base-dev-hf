package org.hf.boot.springboot.retry;

import com.alibaba.fastjson2.JSONObject;
import io.netty.util.internal.ThrowableUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.hf.boot.springboot.pojo.dto.RetryExceptionRequestInfoDTO;
import org.hf.boot.springboot.pojo.entity.RetryExceptionRecord;
import org.hf.boot.springboot.service.RetryExceptionRecordService;
import org.hf.boot.springboot.utils.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * <p> 自定义重试异常 </p >
 * 自定义重试异常实现 - 2
 * @author hufei
 * @date 2023-04-11
 **/
@Slf4j
@Aspect
@Component
public class CustomRetryExceptionAspect {

    @Autowired
    private ThreadPoolTaskExecutor customTaskExecutor;

    @Autowired
    private RetryExceptionRecordService retryExceptionRecordService;

    /**
     * 拦截指定注解,记录异常信息
     * @param point 切点信息
     * @return Object
     * @throws Throwable 异常
     */
    @Around("@annotation(org.hf.boot.springboot.retry.CustomRetryException)")
    public Object saveExceptionRecord(ProceedingJoinPoint point) throws Throwable {
        log.info("进入异常记录切面！");
        try {
            return point.proceed();
        } catch (Throwable e) {
            // 这里异步去记录异常信息
            String errorMsg = ThrowableUtil.stackTraceToString(e);
            HttpServletRequest request = SpringContextUtil.getRequest();
            CompletableFuture.runAsync(() -> asynSaveExceptionRecord(point, errorMsg, request), customTaskExecutor);
            // 如果异常这直接抛出异常
            throw e;
        }
    }

    /**
     * 进行异步记录异常信息
     */
    private void asynSaveExceptionRecord(ProceedingJoinPoint point, String errorMsg, HttpServletRequest request) {
        try{
            MethodSignature signature = (MethodSignature) point.getSignature();
            Method method = signature.getMethod();
            //获取真正的方法对象 否则为代理对象
            CustomRetryException customRetryException = point.getTarget().getClass().getDeclaredMethod(signature.getName(), method.getParameterTypes()).getAnnotation(CustomRetryException.class);
            // 组装异常信息
            RetryExceptionRecord exceptionEntity = new RetryExceptionRecord();
            exceptionEntity.setExceptionMsg(errorMsg);
            // TODO 这里可以根据具体业务进行定义
            exceptionEntity.setBusinessCode(SpringContextUtil.getUuid20());
            exceptionEntity.setServiceCode(customRetryException.serviceCode());
            exceptionEntity.setRequestInfo(JSONObject.toJSONString(builderRequestInfoParams(point, request)));
            exceptionEntity.setRetryCount(0);
            exceptionEntity.setRetryType(customRetryException.retryType().getCode());
            retryExceptionRecordService.saveExceptionRecord(exceptionEntity);
        } catch (Throwable e){
            log.error("添加异常信息失败！", e);
        }
    }

    /**
     * 组装请求信息参数
     */
    private RetryExceptionRequestInfoDTO builderRequestInfoParams(ProceedingJoinPoint point, HttpServletRequest request) {
        RetryExceptionRequestInfoDTO requestInfoDTO = new RetryExceptionRequestInfoDTO();
        if (request != null) {
            requestInfoDTO.setRequestType(request.getMethod());
            requestInfoDTO.setRequestHeaders(getRequestHeaders(request));
            requestInfoDTO.setRequestUrl(request.getRequestURL().toString());
            requestInfoDTO.setRequestPathParams(SpringContextUtil.readQueryStringParameters(request));
            requestInfoDTO.setRequestBodyParams(SpringContextUtil.getBodyString(request));
        }
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        requestInfoDTO.setMethodName(method.getName());
        requestInfoDTO.setServiceName(point.getTarget().getClass().getSimpleName());
        requestInfoDTO.setMethodParams(getParams(point));
        return requestInfoDTO;
    }

    private Map<String, Object> getParams(ProceedingJoinPoint point) {
        Map<String, Object> paramMap = new LinkedHashMap<>();
        // 获取切点的Signature
        MethodSignature methodSignature = (MethodSignature)point.getSignature();
        // 获取切点方法
        Method method = methodSignature.getMethod();
        // 获取切点方法的参数列表
        Parameter[] parameters = method.getParameters();
        // 获取切入点参数
        Object[] args = point.getArgs();
        // 获取方法注解
        CustomRetryException annotation = method.getAnnotation(CustomRetryException.class);
        String[] ignoreParamNames = annotation.ignoreParamNames();
        Class<?>[] classes = annotation.ignoreParamTypes();
        // 封装切点方法参数
        if (parameters == null || args == null || parameters.length != args.length) {
            return paramMap;
        }
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            if (Arrays.stream(ignoreParamNames).anyMatch(paramName -> parameter.getName().equals(paramName)) ||
                    Arrays.stream(classes).anyMatch(paramType -> parameter.getType().isAssignableFrom(paramType))) {
                continue;
            }
            paramMap.put(parameter.getName(), args[i]);
        }
        return paramMap;
    }

    /**
     * 获取请求头信息
     */
    private Map<String, Object> getRequestHeaders(HttpServletRequest request) {
        Map<String, Object> headersMap = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headersMap.put(headerName, request.getHeader(headerName));
        }
        return headersMap;
    }

    /**
     * 判断是否是基础对象或者String类型的对象
     * @param clazz 类型
     * @return boolean
     */
    private boolean isPrimitive(Class<?> clazz) {
        return ClassUtils.isPrimitiveOrWrapper(clazz) || String.class.equals(clazz);
    }
}
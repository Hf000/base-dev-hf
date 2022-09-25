package org.hf.springboot.web.aop;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.hf.common.publi.utils.TimeUtil;
import org.hf.common.web.utils.JacksonUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * <p> 处理异常信息 </p >
 *
 * @author hufei
 * @date 2022-09-05
 **/
@Aspect
@Slf4j
@Component
@ConditionalOnProperty(prefix = "log", value = "enabled", havingValue = "true")
public class CustomLogAspect {

    /**
     * 扫描指定包下的类
     */
    @Pointcut("execution(* org.hf..*.service..*.*(..)) ")
    void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        TimeUtil time = new TimeUtil();
        long deltaTime;
        Object result;
        String clazzName = joinPoint.getTarget().getClass().getSimpleName();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String name = method.getName();
        Object[] args = filterArgs(joinPoint);
        Parameter[] parameters = method.getParameters();
        try {
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            log.error("捕获到异常：{}，类名：{}，方法名：{}，参数：{}，message：{}", throwable, clazzName, name, JacksonUtil.serializer(args), getThrowToString(throwable));
            throw throwable;
        }
        try {
            deltaTime = time.getDeltaTime();
            log.info("方法调用日志，调用时间：{}，类名：{}，方法名：{}", deltaTime, clazzName, name);
            for (int i = 0; i < args.length; i++) {
                log.info("参数列表{}，参数类型：{}，参数值：{}", i, parameters[i].getParameterizedType().getTypeName(), JSONObject.toJSONString(args[i]));
            }
            log.info("方法返回值：{}", JSONObject.toJSONString(result));
        } catch (Throwable throwable) {
            log.error("输出日志异常");
        }
        return result;
    }

    /**
     * 处理入参，过滤MultipartFile
     *
     * @param joinPoint 拦截的切点对象
     * @return Object[]
     */
    private Object[] filterArgs(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args.length == 0) {
            return args;
        }
        Object[] handlerArgs = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof ServletRequest
                    || args[i] instanceof ServletResponse
                    || args[i] instanceof MultipartFile) {
                continue;
            }
            handlerArgs[i] = args[i];
        }
        return handlerArgs;
    }

    /**
     * 将异常堆栈信息转换成字符串
     *
     * @param throwable 异常
     * @return String
     */
    public static String getThrowToString(Throwable throwable) {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        throwable.printStackTrace(printWriter);
        return result.toString();
    }
}
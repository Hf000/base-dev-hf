package org.hf.common.config.expression;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * <p> 自定义注解@CustomParamsAnnotation切面处理 </p>
 * 注解CustomParamsAnnotation实现 - 2
 * @author hufei
 * @version 1.0.0
 * @date 2021/10/12 13:44
 */
@Slf4j
@Aspect
@Component
public class CustomParamsAnnotationAspect {

    /**
     * 切面方法
     * @param customParamsAnnotation 自定义注解
     */
    /*@Pointcut(value = "@annotation(customParamsAnnotation)")
    public void pointcut(CustomParamsAnnotation customParamsAnnotation) {
    }*/

    /**
     * 设置切面为加有注解的方法
     *  采用这种方式一般就不需要上面定义的切面方法了
     * @param joinPoint 根据切面拦截的可以识别的切点方法
     * @param customParamsAnnotation 自定义注解
     * @return Object
     */
    @Around("@annotation(customParamsAnnotation)")
    public Object around(ProceedingJoinPoint joinPoint, CustomParamsAnnotation customParamsAnnotation) {
        return doProcess(joinPoint, customParamsAnnotation);
    }

    /**
     * 切面异常处理
     * @param error 异常
     * @param customParamsAnnotation 注解
     */
    @AfterThrowing(pointcut = "@annotation(customParamsAnnotation)", throwing = "error")
    public void afterThrowing(Throwable error, CustomParamsAnnotation customParamsAnnotation) {
        // 当添加注解@CustomParamsAnnotation的方法处理业务异常时会经过这里处理
        log.error("注解@CustomParamsAnnotation, AOP处理异常:{}", error.getMessage());
    }

    /**
     * 切面处理
     * @param joinPoint 根据切面拦截的可以识别的切点方法
     * @param customParamsAnnotation 注解
     * @return Object
     */
    private Object doProcess(ProceedingJoinPoint joinPoint, CustomParamsAnnotation customParamsAnnotation) {
        Object result = null;
        // 获取拦截的切点方法的参数列表
        Object[] args = joinPoint.getArgs();
        // 得到被代理的方法
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        String[] values = customParamsAnnotation.value();
        String realValue = "";
        for (String value : values) {
            if (StringUtils.isBlank(value)) {
                continue;
            }
            realValue = parseValue(value, method, args);
            // TODO 将方法的参数赋值给注解表达式之后, 可以进行后续自定义场景的操作, 具体业务具体分析
            log.info("@CustomParamsAnnotation通过方法参数获取到的参数的值:{}", realValue);
        }
        try {
            // 执行拦截的方法
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            log.error("@CustomParamsAnnotation解析表达式异常", throwable);
        }
        return result;
    }

    /**
     * 获取缓存的值
     * @param value     定义在注解上的表达式
     * @param method    被拦截的方法
     * @param args      被拦截的方法参数
     * @return 注解定义的参数赋值后返回
     */
    private String parseValue(String value, Method method, Object[] args) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        // 获取被拦截方法参数名列表(使用spring支持类库)
        LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();
        String[] parameterNames = discoverer.getParameterNames(method);
        //对注解上的Spel表达式进行解析
        ExpressionParser parser = new SpelExpressionParser();
        // 获取SPEL上下文
        StandardEvaluationContext seContext = new StandardEvaluationContext();
        // 把方法参数放入SPEL上下文中
        if (parameterNames != null) {
            for (int i = 0; i < parameterNames.length; i++) {
                seContext.setVariable(parameterNames[i], args[i]);
            }
        }
        return parser.parseExpression(value).getValue(seContext, String.class);
    }

}

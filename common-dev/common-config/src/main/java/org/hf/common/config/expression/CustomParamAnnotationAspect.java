package org.hf.common.config.expression;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.expression.EvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * <p> 自定义注解@CustomParamAnnotation切面处理 </p>
 * 注解CustomParamAnnotation实现 - 2
 * @author hufei
 * @version 1.0.0
 * @date 2021/10/12 13:44
 */
@Slf4j
@Aspect
@Component
public class CustomParamAnnotationAspect {

    private final ExpressionEvaluator<String> evaluator = new ExpressionEvaluator<>();

    /**
     * 切面方法
     */
    @Pointcut(value = "@annotation(org.hf.common.config.expression.CustomParamAnnotation)")
    public void pointcut() {
    }

    @Before(value = "pointcut()")
    public void before(JoinPoint joinPoint) {
        // 获取value
        String value = getValue(joinPoint);
        // TODO 将方法的参数赋值给注解表达式之后, 可以进行后续自定义场景的操作, 具体业务具体分析
        log.info("@CustomParamAnnotation注解通过方法参数获取到的参数的值:{}", value);
    }

    @AfterThrowing(value = "pointcut()", throwing = "throwable")
    public void doThrowing(JoinPoint joinPoint, Throwable throwable) {
        // 当添加注解@CustomParamAnnotation的方法处理业务异常时会经过这里处理
        log.error("@CustomParamAnnotation注解AOP异常", throwable);
    }

    private String getValue(JoinPoint joinPoint) {
        CustomParamAnnotation customParamAnnotation = getCustomParamAnnotation(joinPoint);
        if (null == joinPoint.getArgs()) {
            return null;
        }
        EvaluationContext evaluationContext = evaluator.createEvaluationContext(joinPoint.getTarget(), joinPoint.getTarget().getClass(), ((MethodSignature) joinPoint.getSignature()).getMethod(), joinPoint.getArgs());
        AnnotatedElementKey annotatedElementKey = new AnnotatedElementKey(((MethodSignature) joinPoint.getSignature()).getMethod(), joinPoint.getTarget().getClass());
        return evaluator.condition(customParamAnnotation.value(), annotatedElementKey, evaluationContext, String.class);
    }

    private CustomParamAnnotation getCustomParamAnnotation(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        return method.getAnnotation(CustomParamAnnotation.class);
    }

}

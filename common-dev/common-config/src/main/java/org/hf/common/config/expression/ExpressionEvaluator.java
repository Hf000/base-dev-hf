package org.hf.common.config.expression;

import org.springframework.aop.support.AopUtils;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.context.expression.CachedExpressionEvaluator;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>  </p>
 * 注解CustomParamAnnotation实现 - 3
 * @author hufei
 * @version 1.0.0
 * @date 2021/10/12 10:45
 */
public class ExpressionEvaluator<T> extends CachedExpressionEvaluator {

    private final ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
    private final Map<ExpressionKey, Expression> conditionCache = new ConcurrentHashMap<>();
    private final Map<AnnotatedElementKey, Method> targetMethodCache = new ConcurrentHashMap<>();

    public EvaluationContext createEvaluationContext(Object object, Class<?> targetClass, Method method, Object[] objects) {
        Method targetMethod = getTargetMethod(targetClass, method);
        ExpressionRootBO expressionRootBO = new ExpressionRootBO(object, objects);
        return new MethodBasedEvaluationContext(expressionRootBO, targetMethod, objects, this.parameterNameDiscoverer);
    }

    private Method getTargetMethod(Class<?> targetClass, Method method) {
        AnnotatedElementKey annotatedElementKey = new AnnotatedElementKey(method, targetClass);
        Method targetMethod = this.targetMethodCache.get(annotatedElementKey);
        if (null == targetMethod) {
            targetMethod = AopUtils.getMostSpecificMethod(method, targetClass);
            this.targetMethodCache.put(annotatedElementKey, targetMethod);
        }
        return targetMethod;
    }

    public T condition(String conditionExpression, AnnotatedElementKey annotatedElementKey, EvaluationContext evaluationContext, Class<T> clazz) {
        return getExpression(this.conditionCache, annotatedElementKey, conditionExpression).getValue(evaluationContext, clazz);
    }

}

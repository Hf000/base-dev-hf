package org.hf.boot.springboot.aop;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.assertj.core.util.Lists;
import org.hf.boot.springboot.annotations.CustomPrefixRedisLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.expression.MapAccessor;
import org.springframework.core.annotation.Order;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p> 带前缀锁名称的redis锁切面 </p >
 * 2.redis锁注解切面
 * 自定义redis锁CustomPrefixRedisLock实现 - 2
 * @author hufei
 * @date 2023-04-11
 **/
@Aspect
@Component
@Slf4j
@Order(-10000)
@ConditionalOnProperty(name = "redis.lock.enable", havingValue = "true")
public class PrefixRedisLockAspect {

    @Autowired
    private PrefixRedisLockUtil lockUtils;

    @Around("@annotation(org.hf.boot.springboot.annotations.CustomPrefixRedisLock)")
    public Object lockAround(ProceedingJoinPoint joinPoint) {
        List<String> lockNameList;
        String requestId;
        long expiredInMilliSeconds;
        long maxWaitTimeInMilliSeconds;
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        CustomPrefixRedisLock lockAnnotation = methodSignature.getMethod().getDeclaredAnnotation(CustomPrefixRedisLock.class);
        // 1. lockName
        lockNameList = parseLockName(joinPoint, methodSignature, lockAnnotation);
        // 2. requestId
        requestId = lockAnnotation.requestId();
        // 3. expiredInMilliSeconds
        expiredInMilliSeconds = lockAnnotation.expiredInMilliSeconds();
        // 4. maxWaitTimeInMilliSeconds
        maxWaitTimeInMilliSeconds = lockAnnotation.maxWaitTimeInMilliSeconds();
        if (lockNameList.size() == 1) {
            return lockUtils.lockDefault(joinPoint::proceed, lockNameList.get(0), requestId, expiredInMilliSeconds, maxWaitTimeInMilliSeconds);
        } else {
            return lockUtils.multiLockDefault(joinPoint::proceed, lockNameList, requestId, expiredInMilliSeconds, maxWaitTimeInMilliSeconds);
        }
    }

    /**
     * 解析锁的相关入参, 获取锁名称
     * @param joinPoint         切面拦截的切点对象
     * @param methodSignature   切点的方法签名
     * @param lockAnnotation    锁注解对象
     * @return 解析好的锁名称集合
     */
    private List<String> parseLockName(ProceedingJoinPoint joinPoint, MethodSignature methodSignature, CustomPrefixRedisLock lockAnnotation) {
        List<String> lockNameList = new ArrayList<>();
        List<String> lockSuffixList = null;
        // 判断是否是传入的固定锁名称
        if (StringUtils.isBlank(lockAnnotation.lockNameDiff())) {
            // 判断是否是传入的SpEL表达式锁名称
            if (StringUtils.isBlank(lockAnnotation.lockNameDiffExpress())) {
                // 判断是否传入的是多重锁名称SpEL表达式
                if (StringUtils.isNotBlank(lockAnnotation.multiLockNameDiffExpress())) {
                    // 是传入的是多重锁名称SpEL表达式则解析多重锁名称
                    lockSuffixList = parseLockSuffix(joinPoint, methodSignature, lockAnnotation, true);
                }
            } else {
                // 是传入的SpEL表达式锁名称则解析锁名称
                lockSuffixList = parseLockSuffix(joinPoint, methodSignature, lockAnnotation, false);
            }
        } else {
            // 是固定的锁名称则直接获取
            lockSuffixList = Lists.newArrayList(lockAnnotation.lockNameDiff());
        }
        // 组装完整的锁名称
        if (CollectionUtils.isEmpty(lockSuffixList)) {
            // 如果传入的锁名称为空则直接将锁的前缀作为锁名称
            lockNameList.add(lockAnnotation.lockNamePrefix());
        } else {
            for (String lockSuffix : lockSuffixList) {
                lockNameList.add(lockAnnotation.lockNamePrefix() + "_" + lockSuffix);
            }
        }
        return lockNameList;
    }

    /**
     * 解析支持SpEL表达式的锁名称
     * @param joinPoint         切面拦截的切点对象
     * @param methodSignature   切点的方法签名
     * @param lockAnnotation    锁注解对象
     * @param multiLock         是否是多重锁
     * @return 返回解析的锁名称集合
     */
    private List<String> parseLockSuffix(ProceedingJoinPoint joinPoint, MethodSignature methodSignature, CustomPrefixRedisLock lockAnnotation, boolean multiLock) {
        List<String> suffixList;
        try {
            String lockExpression = multiLock ? lockAnnotation.multiLockNameDiffExpress() : lockAnnotation.lockNameDiffExpress();
            Object suffixVal = prepareLockSuffixVal(joinPoint, methodSignature, lockExpression);
            suffixList = doParseLockSuffix(suffixVal, multiLock);
        } catch (Exception e) {
            log.error("解析SpEL表达式出错, expression:{}", lockAnnotation.lockNameDiffExpress(), e);
            suffixList = null;
        }
        return suffixList;
    }

    /**
     * 解析SpEL表达式
     * @param joinPoint         切面拦截的切点对象
     * @param methodSignature   切点的方法签名
     * @param lockExpression    传入的锁名称
     * @return 解析后的对象
     */
    private Object prepareLockSuffixVal(ProceedingJoinPoint joinPoint, MethodSignature methodSignature, String lockExpression) {
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        // 获取拦截方法的参数值
        Object[] args = joinPoint.getArgs();
        // 获取拦截方法的参数名
        String[] parameterNames = methodSignature.getParameterNames();
        Map<String, Object> argsMap = new HashMap<>();
        context.setRootObject(argsMap);
        context.addPropertyAccessor(new MapAccessor());
        for (int i = 0; i < parameterNames.length; i++) {
            argsMap.put(parameterNames[i], args[i]);
        }
        // 将当前调用方法的对象作为 this 放进去，可以获取通过 #{this.XXX} 获取对应的属性（XXX属性需要提供get方法）,
        argsMap.put("this", joinPoint.getThis());
        Expression expression = parser.parseExpression(lockExpression, new TemplateParserContext());
        return expression.getValue(context);
    }

    /**
     * 区分多重锁来解析锁名称
     * @param suffixVal SpEL解析后的对象
     * @param multiLock 是否是多重锁标识
     * @return 返回最终解析好的锁名称
     */
    private List<String> doParseLockSuffix(Object suffixVal, boolean multiLock) {
        List<String> lockSuffixList;
        if (multiLock && suffixVal instanceof Iterable) {
            lockSuffixList = parseMultiLockSuffix((Iterable<?>) suffixVal);
        } else {
            lockSuffixList = Lists.newArrayList(parseSingleLockSuffix(suffixVal));
        }
        return lockSuffixList;
    }

    /**
     * 多重锁的SpEL表达解析后对象转String
     * @param suffixVal SpEL解析后的对象
     * @return List<String>
     */
    private List<String> parseMultiLockSuffix(Iterable<?> suffixVal) {
        Set<String> lockSuffixSet = new HashSet<>();
        for (Object obj : suffixVal) {
            lockSuffixSet.add(parseSingleLockSuffix(obj));
        }
        // 按照字符串自然顺序排序
        return lockSuffixSet.stream().sorted(Comparator.comparing(Object::toString)).collect(Collectors.toList());
    }

    /**
     * 将SpEL表达式解析后的对象转换成字符串
     * @param suffixVal SpEL解析后的对象
     * @return String
     */
    private String parseSingleLockSuffix(Object suffixVal) {
        if (suffixVal == null) {
            return "NULL";
        } else {
            return suffixVal.toString();
        }
    }
}
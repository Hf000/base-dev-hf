package org.hf.common.config.transactional;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * <p> 自定义事务提交注解-2 </p >
 *
 * @author hufei
 * @date 2022-09-22
 **/
@Slf4j
@Aspect
@Component
public class CustomTransactionalAspect {

    /**
     * 事务管理器
     */
    @Autowired
    private PlatformTransactionManager transactionManager;

    /**
     * 事务属性
     */
    @Autowired
    private TransactionDefinition transactionDefinition;

    /**
     * 进行切入点增强
     * ProceedingJoinPoint 仅支持@Around增强, 其他增强类型用JoinPoint,就不需要调proceed方法执行AOP代理链了
     * // @SneakyThrows 抛出已检查的异常
     *
     * @param joinPoint 识别的切点封装对象
     * @return 返回结果
     */
    @SneakyThrows
    @Around("@annotation(CustomTransactional)")
    public Object around(ProceedingJoinPoint joinPoint) {
        // 获取一个事务
        TransactionStatus transaction = transactionManager.getTransaction(transactionDefinition);
        Object proceed = null;
        CustomTransactional methodAnnotation = getMethodAnnotation(joinPoint);
        try {
            proceed = joinPoint.proceed();
            transactionManager.commit(transaction);
        } catch (Exception e) {
            Class<? extends Throwable>[] classes = methodAnnotation.rollbackFor();
            // 判断捕获的异常是否是属于传参异常的子类
            boolean isRollBack = Arrays.stream(classes).anyMatch(clazz -> clazz.isAssignableFrom(e.getClass()));
            if (isRollBack) {
                transactionManager.rollback(transaction);
            } else {
                transactionManager.commit(transaction);
            }
        }
        return proceed;
    }

    /**
     * 获取方法上的注解
     *
     * @param joinPoint 切点封装对象
     * @return CustomTransactional
     */
    private CustomTransactional getMethodAnnotation(ProceedingJoinPoint joinPoint) {
        // 获取切点的Signature
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        // 获取切点方法
        Method method = methodSignature.getMethod();
        // 获取方法上的注解
        return method.getAnnotation(CustomTransactional.class);
    }

}
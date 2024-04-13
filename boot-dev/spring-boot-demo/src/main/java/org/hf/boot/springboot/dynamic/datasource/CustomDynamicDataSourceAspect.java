package org.hf.boot.springboot.dynamic.datasource;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 自定义数据源切换注解切面
 * 自定义多数据源处理 - 6
 */
@Slf4j
@Aspect
@Component
public class CustomDynamicDataSourceAspect {

    @Pointcut("@annotation(org.hf.boot.springboot.dynamic.datasource.CustomDynamicDataSource)")
    public void dynamicDataSource() {
    }

    @Around("dynamicDataSource()")
    public Object datasourceAround(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        CustomDynamicDataSource ds = method.getAnnotation(CustomDynamicDataSource.class);
        if (Objects.nonNull(ds)) {
            DataSourceContextHolder.setDataSource(ds.value());
        }
        try {
            return point.proceed();
        } finally {
            // 移除数据源选择
            DataSourceContextHolder.removeDataSource();
        }
    }
}
package org.hf.common.config.mybatis.pagehelper;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.hf.common.publi.constants.CommonConstant;
import org.hf.common.publi.pojo.dto.BasePageReq;
import org.hf.common.publi.utils.TypeConvertUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p> 自定义mybatis分页注解的aop处理 </p>
 * 自定义分页注解ServicePagination实现 - 2
 * 基于mybatis框架和pagehelper分页插件
 * @author hufei
 * @version 1.0.0
 * @date 2021/11/2 11:02
 */
@Slf4j
@Aspect
@Component
public class ServicePaginationAspect {

    @Pointcut(value = "@annotation(org.hf.common.config.mybatis.pagehelper.ServicePagination)")
    public void assess() {}

    /**
     * 进行切入点增强
     * ProceedingJoinPoint 仅支持@Around增强, 其他增强类型用JoinPoint,就不需要调proceed方法执行AOP代理链了
     * // @SneakyThrows 抛出已检查的异常
     * @param joinPoint 识别的切点封装对象
     * @return 返回结果
     */
    @SneakyThrows
    @Around(value = "assess()")
    public Object around(ProceedingJoinPoint joinPoint) {
        // 获取切入点参数
        Object[] args = joinPoint.getArgs();
        Map<String, Object> paramMap = new HashMap<>(args.length);
        ServicePagination paginationAnnotation = getPaginationAnnotation(joinPoint, paramMap, args);
        // 获取分页参数
        Object pageNo = paramMap.get(paginationAnnotation.pageNo());
        Object pageSize = paramMap.get(paginationAnnotation.pageSize());
        // PageHelper分页处理
        startPageProcess(pageNo == null ? BasePageReq.DEFAULT_PAGE_NO : pageNo, pageSize == null ? BasePageReq.DEFAULT_PAGE_SIZE : pageSize);
        // 执行AOP代理执行链, 调用原本的方法并获取返回值
        Object result = joinPoint.proceed(args);
        return pageResult(result);
    }

    private Object pageResult(Object result) {
        if (result instanceof Page) {
            Page<?> page = TypeConvertUtils.castObject(result, Page.class);
            return new PageInfo<>(page);
        } else if (result instanceof List) {
            return new PageInfo<>((List<?>)result);
        } else {
            return result;
        }
    }

    /**
     * PageHelper分页处理
     * @param pageNoParam   当前页数
     * @param pageSizeParam 页面数据size
     */
    private void startPageProcess(Object pageNoParam, Object pageSizeParam) {
        int pageNo;
        int pageSize;
        if (StringUtils.isNumeric(String.valueOf(pageNoParam)) && StringUtils.isNumeric(String.valueOf(pageSizeParam))) {
            pageNo = Integer.parseInt(String.valueOf(pageNoParam));
            pageSize = Integer.parseInt(String.valueOf(pageSizeParam));
        } else {
            throw new RuntimeException("ServicePagination: pagination parameter type is abnormal");
        }
        //清理 ThreadLocal 存储的分页参数,保证线程安全
        PageHelper.clearPage();
        PageHelper.startPage(pageNo, pageSize);
    }

    /**
     * 获取ServicePagination注解对象, 并返回此方法的参数map
     * @param joinPoint 切点封装对象
     * @param paramMap 切点方法参数map
     * @param args 切点方法参数值
     * @return 分页注解对象
     */
    private ServicePagination getPaginationAnnotation(ProceedingJoinPoint joinPoint, Map<String, Object> paramMap, Object[] args) {
        // 获取切点的Signature
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        // 获取切点方法
        Method method = methodSignature.getMethod();
        // 获取切点方法的参数名称
        String[] parameterNames = methodSignature.getParameterNames();
        if (paramMap == null) {
            paramMap = Maps.newHashMap();
        }
        // 封装切点方法参数
        if (parameterNames != null && args != null && parameterNames.length == args.length) {
            for (int i = 0; i < args.length; i++) {
                paramMap.put(parameterNames[i], args[i]);
            }
        }
        // 获取方法上的注解
        return method.getAnnotation(ServicePagination.class);
    }

}

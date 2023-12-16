package org.hf.boot.springboot.aop;

import io.netty.util.internal.ThrowableUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.hf.boot.springboot.annotations.CustomAvoidRepeat;
import org.hf.boot.springboot.utils.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p> 防重处理过程实现 </p >
 * 2.防重注解切面
 * 自定义防重复注解借助redis实现 - 2
 * @author hufei
 * @date 2023-04-11
 **/
@Aspect
@Component
@Slf4j
@Order(-9999)
@ConditionalOnProperty(name = "redis.enable", havingValue = "true")
public class AvoidRepeatAspect {

    @Autowired
    private StringRedisTemplate template;

    /**
     * 自定义系统应用名称变量
     */
    @Value("${spring.application.name:default}")
    private String applicationName;

    @Around("@annotation(org.hf.boot.springboot.annotations.CustomAvoidRepeat)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        boolean canProceed = true;
        String key = null;
        try {
            String keyNoHashPart = getKeyNoHashPart(methodSignature);
            // 获取请求参数进行
            String keyHashPart = getKeyHashPart(joinPoint.getArgs());
            key = keyNoHashPart + "_" + keyHashPart;
            // 获取失效时间
            long expiredInMilliSeconds = getExpiredInMilliSeconds(methodSignature);
            // 是否设置redis缓存成功
            canProceed = Boolean.TRUE.equals(lock(key, expiredInMilliSeconds));
        } catch (Throwable e) {
            // 异常是否已处理
            log.error("Redis防重失败, class:{}, method:{}, e:{}",
                    methodSignature.getMethod().getDeclaringClass().getCanonicalName(),
                    methodSignature.getMethod().getName(),
                    ThrowableUtil.stackTraceToString(e));
            if (null != key) {
                unLock(key);
            }
        }
        // 这里是为了防止设置redis缓存失败还能正常调用
        if (canProceed) {
            try {
                return joinPoint.proceed();
            } catch (Throwable e) {
                if (null != key) {
                    unLock(key);
                }
                throw e;
            }
        } else {
            throw new RuntimeException("请勿重复请求");
        }
    }

    /**
     * 清除redis缓存
     * @param key 请求的key
     */
    private void unLock(String key) {
        template.opsForValue().getOperations().delete(key);
    }

    /**
     * 设置redis缓存
     * @param key   此次请求的key
     * @param expiredInMilliSeconds 缓存失效时间
     * @return 是否缓存成功
     */
    private Boolean lock(String key, long expiredInMilliSeconds) {
        return template.opsForValue().setIfAbsent(key, "", expiredInMilliSeconds, TimeUnit.MILLISECONDS);
    }

    /**
     * 获取失效时间
     * @param methodSignature 拦截的方法对象
     * @return 返回时间, 单位毫秒
     */
    private long getExpiredInMilliSeconds(MethodSignature methodSignature) {
        CustomAvoidRepeat customAvoidRepeat = methodSignature.getMethod().getAnnotation(CustomAvoidRepeat.class);
        return customAvoidRepeat.value();
    }

    /**
     * 将入参进行md5后hash
     * @param args 切面拦截的方法入参
     * @return String
     */
    private String getKeyHashPart(Object[] args) {
        if (null == args || args.length == 0) {
            return "";
        }
        String argsStr = Arrays.stream(args).map(Object::toString).collect(Collectors.joining(";"));
        return Md5Util.md5Hex(argsStr);
    }

    /**
     * 将请求uri + userId拼接
     * @param methodSignature 切面拦截的方法对象
     * @return String
     */
    private String getKeyNoHashPart(MethodSignature methodSignature) {
        // 服务名称
        String res = applicationName;
        // url
        String url = getUrl();
        if (url == null) {
            // 默认则取类名+方法名
            Method method = methodSignature.getMethod();
            url = method.getDeclaringClass().getCanonicalName() + "#" + method.getName();
        }
        res += url;
        // userId
        CustomAvoidRepeat customAvoidRepeat = methodSignature.getMethod().getAnnotation(CustomAvoidRepeat.class);
        if (customAvoidRepeat.needUserId()) {
            Integer userId = getUserId();
            res += ("_" + userId);
        }
        return res;
    }

    /**
     * 获取当前请求的登录用户id
     * @return 用户id
     */
    private Integer getUserId() {
        // 一般需要从当前请求线程的上下文中获取当前登录用户的id, 一般会在拦截器中处理, 这里暂时固定
        Integer userId = 123;
        if (null == userId) {
            throw new RuntimeException("请先登录");
        }
        return userId;
    }

    /**
     * 获取当前请求的uri
     * @return 请求路径
     */
    private String getUrl() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            throw new RuntimeException("非请求无法使用防重点击注解");
        }
        HttpServletRequest request = requestAttributes.getRequest();
        return request.getRequestURI();
    }
}
package org.hf.boot.springboot.custom.lock.redisson;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * key生成策略
 */
public class RequestKeyGenerator {

    /**
     * 获取LockKey
     *
     * @param joinPoint 切入点
     */
    public static String getLockKey(ProceedingJoinPoint joinPoint) {
        //获取连接点的方法签名对象
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        //Method对象
        Method method = methodSignature.getMethod();
        //获取Method对象上的注解对象
        RequestLock requestLock = method.getAnnotation(RequestLock.class);
        //获取方法参数
        final Object[] args = joinPoint.getArgs();
        // 由于@RequestKeyParam可以放在方法的参数上，也可以放在对象的属性上，所以这里需要进行两次判断，一次是获取方法上的注解，一次是获取对象里面属性上的注解。
        // 获取Method对象参数列表的所有注解
        final Parameter[] parameters = method.getParameters();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parameters.length; i++) {
            final RequestKeyParam keyParam = parameters[i].getAnnotation(RequestKeyParam.class);
            //如果属性不是RequestKeyParam注解，则不处理
            if (keyParam == null) {
                continue;
            }
            //如果属性是RequestKeyParam注解，则拼接 连接符 "& + RequestKeyParam"
            sb.append(requestLock.delimiter()).append(args[i]);
        }
        // 如果参数列表上没有加RequestKeyParam注解, 则获取方法参数列表的对象中的注解
        if (StringUtils.isBlank(sb.toString())) {
            // 获取方法的参数注解数组（为什么是两层数组：因为一个方法可以有多个参数，每个参数都可以有多个注解,第一层表示的是该方法的第几个参数,第二层数组是表示该参数的第几个注解）
            final Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            //循环注解
            for (int i = 0; i < parameterAnnotations.length; i++) {
                final Object object = args[i];
                //获取注解类中所有的属性字段
                final Field[] fields = object.getClass().getDeclaredFields();
                for (Field field : fields) {
                    //判断字段上是否有RequestKeyParam注解
                    final RequestKeyParam annotation = field.getAnnotation(RequestKeyParam.class);
                    //如果没有，跳过
                    if (annotation == null) {
                        continue;
                    }
                    //如果有，设置Accessible为true（为true时可以使用反射访问私有变量，否则不能访问私有变量）
                    field.setAccessible(true);
                    //如果属性是RequestKeyParam注解，则拼接 连接符" & + RequestKeyParam"
                    sb.append(requestLock.delimiter()).append(ReflectionUtils.getField(field, object));
                }
            }
        }
        //返回指定前缀的key
        return requestLock.prefix() + sb;
    }
}
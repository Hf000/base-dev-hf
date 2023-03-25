package org.hf.application.custom.framework.framework.util;

import org.hf.application.custom.framework.framework.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p> 解析自定义指定注解处理 </p>
 * 解析RequestMapping注解
 *
 * @author hufei
 * @date 2022/7/17 19:24
 */
public class ParseAnnotation {

    /**
     * 解析所有RequestMapping注解
     * @return Map<String, Method>具体请求路径对应的方法集合
     * @throws Exception 异常
     */
    public static Map<String, Method> parseRequestMapping() throws Exception {
        //获取要解析的包
        String packagename = XmlBean.scanner();
        //获取包下所有类
        List<Class<?>> classes = LoadClass.getClasses(packagename);
        //获取所有RequestMapping对应的Method的请求路径和方法映射集合
        return parseMapping(classes);
    }

    /**
     * 获取路径和对应的id映射关系
     * @param methodMap 打上@RequestMapping注解的方法和请求路径的集合
     * @param beans     实例化对象的唯一标识id和对象实例集合
     * @return 请求路径和实例对象id的映射集合
     */
    public static Map<String, String> parseUrlMappingInstance(Map<String, Method> methodMap, Map<String, Object> beans) {
        // url ->id
        Map<String, String> urlInstanceMap = new HashMap<>();
        //循环所有方法
        for (Map.Entry<String, Method> methodEntry : methodMap.entrySet()) {
            String url = methodEntry.getKey();
            Class<?> clazz = methodEntry.getValue().getDeclaringClass();
            //循环所有实例
            for (Map.Entry<String, Object> instanceEntry : beans.entrySet()) {
                if (instanceEntry.getValue().getClass() == clazz) {
                    //url ->id
                    urlInstanceMap.put(url, instanceEntry.getKey());
                    break;
                }
            }
        }
        return urlInstanceMap;
    }

    /**
     * 每个注解对应的方法
     * @param clazzList 类集合
     * @return Map<String, Method> 类方法请求路径和对应方法的映射集合
     */
    private static Map<String, Method> parseMapping(List<Class<?>> clazzList) {
        //存储对应的映射关系
        Map<String, Method> handlers = new HashMap<>();
        for (Class<?> clazz : clazzList) {
            //获取类上的注解
            RequestMapping typeAnnotation = clazz.getAnnotation(RequestMapping.class);
            //类上注解的值
            String typeAnnotationName = "";
            if (typeAnnotation != null) {
                typeAnnotationName = typeAnnotation.value();
            }
            //获取所有方法
            Method[] methods = clazz.getDeclaredMethods();
            //循环所有方法
            for (Method method : methods) {
                //获取方法上的注解
                RequestMapping methodAnnotation = method.getAnnotation(RequestMapping.class);
                if (methodAnnotation != null) {
                    //获取注解的值
                    String methodAnnotationValue = methodAnnotation.value();
                    System.out.println(typeAnnotationName + methodAnnotationValue + "----->" + method.getDeclaringClass().getName() + "." + method.getName());
                    //将映射数据存入到Map中
                    handlers.put(typeAnnotationName + methodAnnotationValue, method);
                }
            }
        }
        return handlers;
    }
}
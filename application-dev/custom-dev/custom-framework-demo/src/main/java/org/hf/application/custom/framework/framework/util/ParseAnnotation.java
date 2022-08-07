package org.hf.application.custom.framework.framework.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>  </p>
 * 解析RequestMapping注解
 * @author hufei
 * @date 2022/7/17 19:24
*/
public class ParseAnnotation {

    /***
     * 解析所有RequestMapping注解
     * @return
     */
    public static Map<String,Method> parseRequestMapping() throws Exception {
        //获取要解析的包
        String packagename = XmlBean.scanner();
        //获取所有RequestMapping对应的Method
        List<Class<?>> classes = LoadClass.getClasses(packagename);
        Map<String, Method> methodMap = parseMapping(classes);
        return methodMap;
    }

    /****
     * 获取路径和对应的id映射关系
     */
    public static Map<String,String> parseUrlMappingInstance(Map<String, Method> methodMap,Map<String, Object> beans){
        // url ->id
        Map<String,String> urlInstanceMap = new HashMap<String,String>();

        //循环所有方法
        for (Map.Entry<String, Method> methodEntry : methodMap.entrySet()) {
            String url = methodEntry.getKey();
            Class<?> clazz = methodEntry.getValue().getDeclaringClass();
            //循环所有实例
            for (Map.Entry<String, Object> instanceEntry : beans.entrySet()) {
                if(instanceEntry.getValue().getClass()==clazz){
                    //url ->id
                    urlInstanceMap.put(url,instanceEntry.getKey());
                    break;
                }
            }
        }
        return urlInstanceMap;
    }

    /****
     * 每个注解对应的方法
     * @param clazzes
     * @return
     */
    private static Map<String, Method> parseMapping(List<Class<?>> clazzes){
        //存储对应的映射关系
        Map<String,Method> handlers = new HashMap<String,Method>();
        for (Class clazz : clazzes) {
            //获取类上的注解
            RequestMapping typeAnnotation = (RequestMapping) clazz.getAnnotation(RequestMapping.class);

            //类上注解的值
            String typeAnnotationName="";
            if(typeAnnotation!=null){
                typeAnnotationName = typeAnnotation.value();
            }

            //获取所有方法
            Method[] methods = clazz.getDeclaredMethods();

            //循环所有方法
            for (Method method : methods) {
                //获取方法上的注解
                RequestMapping methodAnnotation = method.getAnnotation(RequestMapping.class);
                if(methodAnnotation!=null){
                    //获取注解的值
                    String methodAnnotationValue = methodAnnotation.value();
                    System.out.println(typeAnnotationName + methodAnnotationValue+"                      "+method.getDeclaringClass().getName()+"."+method.getName());
                    //将映射数据存入到Map中
                    handlers.put(typeAnnotationName + methodAnnotationValue,method);
                }
            }
        }
        return handlers;
    }
}

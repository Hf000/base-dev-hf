package org.hf.boot.springboot.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hf.boot.springboot.annotations.CustomAuthorizationCheck;
import org.hf.boot.springboot.pojo.dto.RequestMappingDTO;
import org.hf.boot.springboot.pojo.dto.SheetDTO;
import org.hf.boot.springboot.utils.ExcelUtil;
import org.hf.boot.springboot.utils.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Api("遍历获取url资源相关接口")
@Slf4j
@RestController
@RequestMapping("/urlRes")
public class UrlResController {

    @Autowired
    private WebApplicationContext webApplicationContext;
    
    public static final String COMMA = ",";

    private static final String MICRO_SRV = "custom服务";

    private final List<String> CUSTOM_FIELD_LIST = CollectionUtil.newArrayList("id");
    
    @ApiOperation("遍历URL资源")
    @GetMapping("/scanUrlRes")
    public void scanUrlRes() {
        List<RequestMappingDTO> requestMappingList = scanRequestMapping();
        log.info("requestMappingList:{}", requestMappingList);
        String sheetName = "URL资源";
        String fileName = sheetName + DateUtil.format(new Date(), "yyyyMMddHHmm");
        SheetDTO sheetDto = new SheetDTO();
        sheetDto.setSheetName(sheetName);
        sheetDto.setHeadClass(RequestMappingDTO.class);
        sheetDto.setDataList(requestMappingList);
        try {
            ExcelUtil.exportExcel(fileName, sheetDto, SpringContextUtil.getResponse());
        } catch (IOException e) {
            log.error("遍历URL资源文件导出异常", e);
        }
    }

    @ApiOperation("遍历包含字段字段出入参的url资源")
    @GetMapping("/scanCustomFieldRes")
    public void scanCustomFieldRes() {
        List<RequestMappingDTO> requestMappingList = scanRequestMapping();
        // 过滤,取返回字段中含有手机号,身份证号的接口
        requestMappingList = requestMappingList.stream().filter(requestMappingDto ->
                StrUtil.isNotBlank(requestMappingDto.getOutParamCustomFields())).collect(Collectors.toList());
        log.info("requestMappingList:{}", requestMappingList);
        String sheetName = "出入参带敏感字段信息的URL资源";
        String fileName = sheetName + DateUtil.format(new Date(), "yyyyMMddHHmm");
        SheetDTO sheetDto = new SheetDTO();
        sheetDto.setSheetName(sheetName);
        sheetDto.setHeadClass(RequestMappingDTO.class);
        sheetDto.setDataList(requestMappingList);
        try {
            ExcelUtil.exportExcel(fileName, sheetDto, SpringContextUtil.getResponse());
        } catch (IOException e) {
            log.info("IOException：", e);
        }
    }

    /**
     * 扫描在SpringMVC注册接口信息
     */
    private List<RequestMappingDTO> scanRequestMapping() {
        // 映射处理器的映射信息
        RequestMappingHandlerMapping requestMappingHandlerMapping = webApplicationContext.getBean(RequestMappingHandlerMapping.class);
        // 映射处理器中的所有方法
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
        log.info("handlerMethods:{}", handlerMethods);
        List<RequestMappingDTO> requestMappingList = new ArrayList<>();
        handlerMethods.forEach((k, v) -> {
            RequestMappingDTO requestMappingDto;
            try {
                requestMappingDto = bindRequestMapping(k, v);
                if (requestMappingDto == null) {
                    return;
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            requestMappingList.add(requestMappingDto);
        });
        return requestMappingList;
    }

    /**
     * 绑定RequestMapping 适用于一个Controller方法上只有一个RequestMapping的场景
     */
    private RequestMappingDTO bindRequestMapping(RequestMappingInfo requestMappingInfo, HandlerMethod handlerMethod) throws ClassNotFoundException {
        RequestMappingDTO requestMappingDto = new RequestMappingDTO();
        PatternsRequestCondition patternsCondition = requestMappingInfo.getPatternsCondition();
        if (patternsCondition == null) {
            log.info("绑定controller接口url路径时出错, requestMappingInfo={}", requestMappingInfo);
            return null;
        }
        Set<String> urlPatternSet = patternsCondition.getPatterns();
        // 取其中一个urlMapping
        urlPatternSet.forEach(requestMappingDto::setUrlMapping);
        requestMappingDto.setMicroSrv(MICRO_SRV);
        requestMappingDto.setRelPackage(handlerMethod.getBeanType().getPackage().getName().replace("org.hf", ""));
        requestMappingDto.setController(handlerMethod.getBeanType().getSimpleName());
        requestMappingDto.setUrlPattern(transUrlMappingToUrlPattern(requestMappingDto.getUrlMapping()));
        requestMappingDto.setUrlAccessCheck(checkUrlAccessCheckAnnotation(handlerMethod.getBeanType(), handlerMethod.getMethod()));
        requestMappingDto.setGroupName(getApiAnnotationValue(handlerMethod.getBeanType()));
        requestMappingDto.setUrlResName(getApiOperationAnnotationValue(handlerMethod.getMethod()));
        requestMappingDto.setInParamFields(getInParamFields(handlerMethod));
        requestMappingDto.setOutParamFields(getOutParamFields(handlerMethod));
        requestMappingDto.setOutParamCustomFields(getOutParamCustomFields(handlerMethod));
        requestMappingDto.setInParamCustomFields(getInParamCustomFields((handlerMethod)));
        return requestMappingDto;
    }

    /**
     * 判断接口是否有权限校验
     */
    private boolean checkUrlAccessCheckAnnotation(Class<?> beanType, Method handlerMethod) {
        // 根据指定权限注解类判断接口是否有权限校验
        boolean isTypeAnnotationPresent = beanType.isAnnotationPresent(CustomAuthorizationCheck.class);
        boolean isMethodAnnotationPresent = handlerMethod.isAnnotationPresent(CustomAuthorizationCheck.class);
        log.info("beanType:{}, isTypeAnnotationPresent:{}, isMethodAnnotationPresent:{}", beanType.getSimpleName(), isTypeAnnotationPresent, isMethodAnnotationPresent);
        return isTypeAnnotationPresent || isMethodAnnotationPresent;
    }

    /**
     * 根据swagger注解获取controller类的接口描述
     */
    private String getApiAnnotationValue(Class<?> beanType) {
        boolean isTypeAnnotationPresent = beanType.isAnnotationPresent(Api.class);
        log.info("isTypeAnnotationPresent:{}", isTypeAnnotationPresent);
        if (!isTypeAnnotationPresent) {
            return null;
        }
        Api annotation = beanType.getAnnotation(Api.class);
        return annotation.tags()[0];
    }

    /**
     * 根据swagger注解获取接口描述
     */
    private String getApiOperationAnnotationValue(Method handlerMethod) {
        boolean isMethodAnnotationPresent = handlerMethod.isAnnotationPresent(ApiOperation.class);
        log.info("getApiOperationAnnotationValue:{}", isMethodAnnotationPresent);
        if (!isMethodAnnotationPresent) {
            return null;
        }
        ApiOperation annotation = handlerMethod.getAnnotation(ApiOperation.class);
        return annotation.value();
    }

    /**
     * 获取接口入参字段
     */
    private String getInParamFields(HandlerMethod handlerMethod) {
        Parameter[] parameters = handlerMethod.getMethod().getParameters();
        if (parameters.length == 0) {
            return "";
        }
        List<String> fieldNameList = new ArrayList<>();
        Arrays.stream(parameters).forEach(parameter -> {
            Class<?> type = parameter.getType();
            if (isBaseType(type)) {
                fieldNameList.add(parameter.getName());
                // 跳过此参数
                return;
            }
            Field[] fields = type.getDeclaredFields();
            if (fields.length > 0) {
                log.info("fieldName:{}", fields[0].getName());
                Arrays.stream(fields).forEach(field -> fieldNameList.add(field.getName()));
            }
        });
        return String.join(COMMA, fieldNameList);
    }

    /**
     * 获取接口出参字段
     */
    private String getOutParamFields(HandlerMethod handlerMethod) throws ClassNotFoundException {
        Type genericReturnType = handlerMethod.getMethod().getGenericReturnType();
        Type finalActualTypeFromGenericType = getFinalActualTypeFromGenericType(genericReturnType);
        List<String> fieldNameList = new ArrayList<>();
        try {
            Class<?> finalActualClazz = Class.forName(finalActualTypeFromGenericType.getTypeName());
            if (isBaseType(finalActualClazz)) {
                return finalActualClazz.getName();
            }
            Field[] declaredFields = finalActualClazz.getDeclaredFields();
            if (declaredFields.length == 0) {
                return null;
            }
            Arrays.stream(declaredFields).forEach(field -> {
                fieldNameList.add(field.getName());
            });
            return String.join(COMMA, fieldNameList);
        } catch (ClassNotFoundException e) {
            return "";
        }
    }

    private String getOutParamCustomFields(HandlerMethod handlerMethod) throws ClassNotFoundException {
        Type genericReturnType = handlerMethod.getMethod().getGenericReturnType();
        Type finalActualTypeFromGenericType = getFinalActualTypeFromGenericType(genericReturnType);
        List<String> fieldNameList = new ArrayList<>();
        try {
            Class<?> finalActualClazz = Class.forName(finalActualTypeFromGenericType.getTypeName());
            if (isBaseType(finalActualClazz)) {
                // 返回基本类型,没法判定
                return null;
            }
            Field[] declaredFields = finalActualClazz.getDeclaredFields();
            if (declaredFields.length == 0) {
                return null;
            }
            Arrays.stream(declaredFields).forEach(field -> {
                String fieldName = field.getName();
                boolean flag = containsCustomFields(fieldName, CUSTOM_FIELD_LIST);
                if (flag) {
                    fieldNameList.add(fieldName);
                }
            });
            return String.join(COMMA, fieldNameList);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    private String getInParamCustomFields(HandlerMethod handlerMethod) throws ClassNotFoundException {
        Parameter[] parameters = handlerMethod.getMethod().getParameters();
        if (parameters.length == 0) {
            return "";
        }
        List<String> fieldNameList = new ArrayList<>();
        Arrays.stream(parameters).forEach(parameter -> {
            Class<?> type = parameter.getType();
            if (isBaseType(type)) {
                String fieldName = parameter.getName();
                boolean flag = containsCustomFields(fieldName, CUSTOM_FIELD_LIST);
                if (flag) {
                    fieldNameList.add(fieldName);
                }
                // 跳过此参数
                return;
            }
            Field[] fields = type.getDeclaredFields();
            if (fields.length > 0) {
                log.info("fieldName:{}", fields[0].getName());
                Arrays.stream(fields).forEach(field -> {
                    String fieldName = field.getName();
                    boolean flag = containsCustomFields(fieldName, CUSTOM_FIELD_LIST);
                    if (flag) {
                        fieldNameList.add(fieldName);
                    }
                });
            }
        });
        return String.join(COMMA, fieldNameList);
    }

    /**
     * urlMapping转Ant风格urlPattern
     */
    @SuppressWarnings("all")
    private String transUrlMappingToUrlPattern(String urlMapping) {
        return "/" + MICRO_SRV + urlMapping.replaceAll("\\{((?!\\{).)*\\}", urlMapping);
    }

    /**
     * 获取最内层泛型类型
     */
    private Type getFinalActualTypeFromGenericType(Type type) throws ClassNotFoundException {
        if (type instanceof ParameterizedType) {
            Type actualTypeFromGenericType = getActualTypeFromGenericType(type);
            if (actualTypeFromGenericType != null) {
                return getFinalActualTypeFromGenericType(actualTypeFromGenericType);
            } else {
                return type;
            }
        }
        return type;
    }

    /**
     * 获取第一层泛型类型
     */
    private Type getActualTypeFromGenericType(Type type) throws ClassNotFoundException {
        if (type instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
            // 取泛型的第一个参数
            return actualTypeArguments[0];
        }
        return null;
    }

    /**
     * 判断属性类型
     */
    private boolean isBaseType(Class<?> clazz) {
        return clazz.equals(Integer.class) || clazz.equals(Long.class)
                || clazz.equals(Byte.class) || clazz.equals(Boolean.class)
                || clazz.equals(Character.class) || clazz.equals(String.class);
    }

    private boolean containsCustomFields(String fieldName, List<String> customFieldList) {
        if (StrUtil.isBlank(fieldName)) {
            return false;
        }
        fieldName = fieldName.toLowerCase();
        for (String customField : customFieldList) {
            if (fieldName.contains(customField)) {
                return true;
            }
        }
        return false;
    }
}
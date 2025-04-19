package org.hf.boot.springboot.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hf.boot.springboot.pojo.dto.SheetDTO;
import org.hf.boot.springboot.utils.ExcelUtil;
import org.hf.boot.springboot.utils.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/urlRes/ResolveInfo")
public class UrlResResolveController {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private static final List<String> CUSTOM_CHAR_FIELD_LIST = Arrays.asList("mobile", "phone", "idNo", "email", "address");

    private static final String MICRO_SRV = "custom服务";

    private final static String FIELD_ID = "id";

    public static final String COMMA = ",";

    @ApiOperation("遍历URL资源")
    @GetMapping("/scanUrlRes")
    public void scanUrlRes() {
        List<RequestMappingDTO> requestMappingList = scanRequestMapping();
//        // 过滤出没有加注解的资源
//        requestMappingList = requestMappingList.stream().filter(requestMappingDTO ->
//                StrUtil.isNotBlank(requestMappingDTO.getInParamIncludeFieldsNotAnnotation()) ||
//                        StrUtil.isNotBlank(requestMappingDTO.getOutParamIncludeFieldsNotAnnotation())).collect(Collectors.toList());
        log.info("requestMappingList:{}", requestMappingList);
        String sheetName = MICRO_SRV + "URL资源排查";
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
     * 通过SpringMVC请求处理映射器获取所有的接口资源
     */
    private List<RequestMappingDTO> scanRequestMapping() {
        // 映射处理器的映射信息, 在spring boot2.7.x以上版本可能出现多个实现, 所以这里最好指定一下bean名称
        RequestMappingHandlerMapping requestMappingHandlerMapping = webApplicationContext.getBean("requestMappingHandlerMapping",
                RequestMappingHandlerMapping.class);
        // 映射处理器中的所有方法
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
        log.info("handlerMethods:{}", handlerMethods);
        List<RequestMappingDTO> requestMappingList = new ArrayList<>();
        handlerMethods.forEach((k, v) -> {
            RequestMappingDTO requestMappingDTO;
            try {
                requestMappingDTO = bindRequestMapping(k, v);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            requestMappingList.add(requestMappingDTO);
        });
        return requestMappingList;
    }

    /**
     * 绑定RequestMapping 适用于一个Controller方法上只有一个RequestMapping的场景
     */
    private RequestMappingDTO bindRequestMapping(RequestMappingInfo requestMappingInfo, HandlerMethod handlerMethod)
            throws ClassNotFoundException {
        RequestMappingDTO requestMappingDTO = new RequestMappingDTO();
        // 通过具体资源映射器的请求信息获取对应的请求路径
        Set<String> urlPatternSet = requestMappingInfo.getPatternValues();
        // 这里只考虑资源只有一个请求映射的情况, 所以取其中一个urlMapping
        urlPatternSet.forEach(requestMappingDTO::setUrlMapping);
        log.info("解析请求接口路径={}", requestMappingDTO.getUrlMapping());
        requestMappingDTO.setController(handlerMethod.getBeanType().getSimpleName());
        requestMappingDTO.setUrlResName(getApiOperationAnnotationValue(handlerMethod.getMethod()));
        requestMappingDTO.setInParamIncludeFieldsNotAnnotation(getInParamIncludeFieldsNotAnnotation(handlerMethod));
        requestMappingDTO.setOutParamIncludeFieldsNotAnnotation(getOutParamIncludeFieldsNotAnnotation(handlerMethod));
        requestMappingDTO.setInParamMissingAnnotation(getInParamMissingAnnotation(handlerMethod));
        requestMappingDTO.setOutParamMissingAnnotation(getOutParamMissingAnnotation(handlerMethod));
        requestMappingDTO.setInParamCustomFields(getInParamCustomFields(handlerMethod));
        requestMappingDTO.setOutParamCustomFields(getOutParamCustomFields(handlerMethod));
        requestMappingDTO.setInParamFields(getInParamFields(handlerMethod));
        requestMappingDTO.setOutParamFields(getOutParamFields(handlerMethod));
        return requestMappingDTO;
    }

    /**
     * 获取接口的所有出参，嵌套扫描里层对象属性
     * @param handlerMethod 处理器方法对象
     * @return 出参拼接字符串
     */
    private String getOutParamFields(HandlerMethod handlerMethod) {
        Type genericReturnType = handlerMethod.getMethod().getGenericReturnType();
        Type finalActualTypeFromGenericType = getFinalActualTypeFromGenericType(genericReturnType);
        List<String> fieldNameList = new ArrayList<>();
        try {
            Class<?> finalActualClazz = Class.forName(finalActualTypeFromGenericType.getTypeName());
            if (isReturnBaseType(finalActualClazz)) {
                // 返回基本类型
                return finalActualClazz.getName();
            }
            // 循环获取属性不为基础包装对象类型的所有属性字段
            loopObjectAllField(finalActualClazz, fieldNameList);
            if (CollectionUtils.isEmpty(fieldNameList)) {
                return null;
            }
            return String.join(COMMA, fieldNameList);
        } catch (ClassNotFoundException e) {
            return "";
        }
    }

    /**
     * 获取接口的入参,嵌套扫描
     * @param handlerMethod 处理器方法对象
     * @return 入参拼接字符串
     */
    private String getInParamFields(HandlerMethod handlerMethod) {
        Parameter[] parameters = handlerMethod.getMethod().getParameters();
        if (parameters.length == 0) {
            return "";
        }
        List<String> fieldNameList = new ArrayList<>();
        Arrays.stream(parameters).forEach(parameter -> {
            Class<?> type = parameter.getType();
            if (isReturnBaseType(type)) {
                fieldNameList.add(parameter.getName());
                // 跳过此参数
                return;
            }
            // 循环获取属性不为基础包装对象类型的所有属性字段
            loopObjectAllField(parameter.getType(), fieldNameList);
        });
        if (CollectionUtils.isEmpty(fieldNameList)) {
            return null;
        }
        return String.join(COMMA, fieldNameList);
    }

    private static void loopObjectAllField(Class<?> type, List<String> fieldNameList) {
        // 第一个参数是当前对象类型
        // 第二个参数是装载所有的字段的
        // 第三个参数是为了拼接完整的类属性引用的
        // 第四个参数是为了防止循环扫描的
        // 第五个参数是用来控制嵌套扫描的层数
        loopObjectAllField(type, fieldNameList, new ArrayList<>(), new ArrayList<>(), 2);
    }

    private static void loopObjectAllField(Class<?> type, List<String> fieldNameList, List<String> refClassName,
                                           List<Class<?>> refClass, int scanNum) {
        String simpleName = type.getSimpleName();
        refClassName.add(simpleName);
        refClass.add(type);
        Field[] fields = type.getDeclaredFields();
        for (Field field : fields) {
            Class<?> fieldType = field.getType();
            if (isListType(fieldType) && scanNum > 0) {
                Class<?> aClass = getParamListType(field);
                if (aClass == null || refClass.contains(aClass) || isReturnBaseType(aClass)) {
                    // 这里先不加类信息,可能导致超出excel单元格的最大字符限制
//                    fieldNameList.add(StringUtils.join(refClassName, "#") + "##" +field.getName());
                    fieldNameList.add(field.getName());
                    continue;
                }
                loopObjectAllField(aClass, fieldNameList, refClassName, refClass, scanNum - 1);
            } else if (!refClass.contains(fieldType) && !isReturnBaseType(fieldType) && scanNum > 0) {
                loopObjectAllField(fieldType, fieldNameList, refClassName, refClass, scanNum - 1);
            } else {
                // 这里先不加类信息,可能导致超出excel单元格的最大字符限制
//                fieldNameList.add(StringUtils.join(refClassName, "#") + "##" +field.getName());
                fieldNameList.add(field.getName());
            }
        }
        refClassName.remove(simpleName);
    }

    /**
     * 通过swagger注解获取改接口的描述
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
     * 获取请求接口所有入参名称是否包含指定字符串并缺少指定注解的入参信息
     */
    private String getInParamIncludeFieldsNotAnnotation(HandlerMethod handlerMethod) {
        Parameter[] parameters = handlerMethod.getMethod().getParameters();
        if (parameters.length == 0) {
            return null;
        }
        List<String> fieldNameList = new ArrayList<>();
        Arrays.stream(parameters).forEach(parameter -> {
            Class<?> type = parameter.getType();
            if (isReturnBaseType(type)) {
                // 跳过此参数 无法加日志脱敏注解
                return;
            }
            loopIncludeFiledInfo(type, fieldNameList);
        });
        if (CollectionUtils.isEmpty(fieldNameList)) {
            return null;
        }
        return String.join(COMMA, fieldNameList);
    }

    private static void loopIncludeFiledInfo(Class<?> type, List<String> fieldNameList) {
        loopIncludeFiledInfo(type, fieldNameList, new ArrayList<>(), new ArrayList<>());
    }

    /**
     * 递归获取对象中的嵌套对象属性名称是否包含字段字符,且属性是否添加了指定注解
     */
    private static void loopIncludeFiledInfo(Class<?> type, List<String> fieldNameList, List<String> refClassNames,
                                             List<Class<?>> refClass) {
        String simpleName = type.getSimpleName();
        refClassNames.add(simpleName);
        refClass.add(type);
        Field[] fields = type.getDeclaredFields();
        for (Field field : fields) {
            Class<?> fieldType = field.getType();
            if (isCustomType(fieldType)) {
                // 判断出参是否有敏感字段且没加日志脱敏注解
                if (checkIncludeFieldsNotAnnotation(field)) {
                    fieldNameList.add(StringUtils.join(refClassNames, "#") + "##" + field.getName());
                }
            }  else if (isListType(fieldType)) {
                Class<?> aClass = getParamListType(field);
                if (aClass == null || refClass.contains(aClass) || isReturnBaseType(aClass)) {
                    continue;
                }
                loopIncludeFiledInfo(aClass, fieldNameList, refClassNames, refClass);
            } else if (!refClass.contains(fieldType) && !isReturnBaseType(fieldType)) {
                loopIncludeFiledInfo(fieldType, fieldNameList, refClassNames, refClass);
            }
        }
        refClassNames.remove(simpleName);
    }

    /**
     * 判断字段是否包含指定字符并没有加指定注解
     */
    private static boolean checkIncludeFieldsNotAnnotation(Field field) {
        // 判断是否加了指定注解
        FieldEnum annotation = field.getAnnotation(FieldEnum.class);
        boolean isInclude = containsCustomCharFields(field.getName(), CUSTOM_CHAR_FIELD_LIST);
        return isInclude && annotation == null;
    }

    /**
     * 获取请求接口出参中字段名称是否包含指定字符串并缺少指定注解的出参字段信息
     */
    private String getOutParamIncludeFieldsNotAnnotation(HandlerMethod handlerMethod) {
        Type genericReturnType = handlerMethod.getMethod().getGenericReturnType();
        Type finalActualTypeFromGenericType = getFinalActualTypeFromGenericType(genericReturnType);
        try {
            Class<?> finalActualClazz = Class.forName(finalActualTypeFromGenericType.getTypeName());
            if (isReturnBaseType(finalActualClazz)) {
                // 没法判定
                return null;
            }
            List<String> fieldNameList = new ArrayList<>();
            loopIncludeFiledInfo(finalActualClazz, fieldNameList);
            if (CollectionUtils.isEmpty(fieldNameList)) {
                return null;
            }
            return String.join(COMMA, fieldNameList);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    /**
     * 获取入参类或入参属性上没有加指定注解
     */
    private String getInParamMissingAnnotation(HandlerMethod handlerMethod) {
        Parameter[] parameters = handlerMethod.getMethod().getParameters();
        if (parameters.length == 0) {
            return null;
        }
        List<String> classNameList = new ArrayList<>();
        List<String> classFieldNameList = new ArrayList<>();
        Arrays.stream(parameters).forEach(parameter -> {
            Class<?> type = parameter.getType();
            if (isReturnBaseType(type)) {
                return;
            }
            getClassInfo(type, classNameList, classFieldNameList);
        });
        return getResultString(classNameList, classFieldNameList);
    }

    /**
     * 获取组装结果
     */
    private static String getResultString(List<String> classNameList, List<String> classFieldNameList) {
        String result = "";
        if (CollectionUtils.isNotEmpty(classNameList)) {
            result = "类上未加FieldClass注解:" + String.join(COMMA, classNameList) + ";";
        }
        if (CollectionUtils.isNotEmpty(classFieldNameList)) {
            result += "属性对象上未加FieldAttribute注解:" + String.join(COMMA, classFieldNameList) + ";";
        }
        return result;
    }

    /**
     * 获取出/入参最外层对象是否有加指定注解
     */
    private static void getClassInfo(Class<?> type, List<String> classNameList, List<String> classFieldNameList) {
        String simpleName = type.getSimpleName();
        List<String> refClassNames = Lists.newArrayList(simpleName);
        List<Class<?>> refClass = Lists.newArrayList(type);
        Field[] fields = type.getDeclaredFields();
        boolean flag = true;
        for (Field field : fields) {
            // 同一个实体对象, 这里只需要判断一次
            if (flag && checkClassNotAnnotation(field)) {
                FieldClass annotation = type.getAnnotation(FieldClass.class);
                if (annotation == null) {
                    classNameList.add(simpleName);
                }
                flag = false;
            }
            // 如果不是基本数据类型则递归遍历
            handleCustomObj(field, classNameList, classFieldNameList, type, refClassNames, refClass);
        }
    }

    /**
     * 处理自定义对象时的指定注解检查
     */
    private static void handleCustomObj(Field field, List<String> classNameList,
                                        List<String> classFieldNameList, Class<?> parentTypeParam,
                                        List<String> refClassNames, List<Class<?>> refClass) {
        if (isListType(field.getType())) {
            // 如果是List集合则需要获取集合的泛型真实类型后判断是否遗漏了脱敏注解
            Class<?> aClass = getParamListType(field);
            if (aClass == null || isReturnBaseType(aClass)) {
                return;
            }
            if (refClass.contains(aClass)) {
                checkedObjCustomAnno(field, aClass, classFieldNameList, refClassNames);
                return;
            }
            loopFieldClassInfo(field, aClass, classNameList, classFieldNameList, parentTypeParam, refClassNames, refClass);
        } else if (!isReturnBaseType(field.getType())) {
            if (refClass.contains(field.getType())) {
                checkedObjCustomAnno(field, field.getType(), classFieldNameList, refClassNames);
                return;
            }
            // 如果是自定义的实体对象判断是否遗漏了脱敏注解
            loopFieldClassInfo(field, null, classNameList, classFieldNameList, parentTypeParam, refClassNames, refClass);
        }
    }

    /**
     * 如果已经处理过的自定义对象, 则只检查当前引用和当前引用类型上是否有对应注解。
     * 第一个参数和第二个参数解释:这里为什么要区分字段field对象和字段类型对象type, 因为当前字段field为集合时,
     * 需要获取集合的泛型类型而不是字段类型。
     */
    private static void checkedObjCustomAnno(Field field, Class<?> type, List<String> classFieldNameList,
                                             List<String> refClassNames) {
        FieldClass annotation1 = type.getAnnotation(FieldClass.class);
        if (annotation1 != null && field.getAnnotation(FieldAttribute.class) == null) {
            classFieldNameList.add(StringUtils.join(refClassNames, "#") + "##" + field.getName());
        }
    }

    /***
     * 递归获取出/入参嵌套对象是否添加指定注解。
     * 第一个参数和第二个参数解释:这里为什么要区分字段fieldParam对象和字段类型对象fieldTypeParam, 因为当前字段fieldParam为集合时,
     * 需要获取集合的泛型类型而不是字段类型。
     */
    private static void loopFieldClassInfo(Field fieldParam, Class<?> fieldTypeParam, List<String> classNameList,
                                           List<String> classFieldNameList, Class<?> parentTypeParam,
                                           List<String> refClassNames, List<Class<?>> refClass) {
        Class<?> type;
        if (fieldTypeParam != null) {
            type = fieldTypeParam;
        } else {
            type = fieldParam.getType();
        }
        String simpleName = type.getSimpleName();
        refClassNames.add(simpleName);
        refClass.add(type);
        Field[] fields = type.getDeclaredFields();
        boolean flag = true;
        for (Field field : fields) {
            // 同一个实体对象, 这里只需要判断一次
            if (flag && checkClassNotAnnotation(field)) {
                // 判断当前类是否添加了指定注解
                FieldClass annotation1 = type.getAnnotation(FieldClass.class);
                if (annotation1 == null ) {
                    classNameList.add(StringUtils.join(refClassNames, "#"));
                }
                // 判断当前类是成员变量属性时是否加了指定注解
                FieldAttribute annotation2 = fieldParam.getAnnotation(FieldAttribute.class);
                if (annotation2 == null) {
                    // 这里拼接类时需要减一的原因是：判断的是当前字段的类对象是否缺少了指定注解
                    List<String> parentTypeNames = refClassNames.stream().limit(refClassNames.size() - 1).collect(Collectors.toList());
                    String parentTypeNameStrs = StringUtils.join(parentTypeNames, "#");
                    classFieldNameList.add(parentTypeNameStrs + "##" + fieldParam.getName());
                    // 判断上层类是否添加了日志脱敏注解
//                    FieldClass annotation3 = parentTypeParam.getAnnotation(FieldClass.class);
//                    if (annotation3 == null ) {
//                        classNameList.add(parentTypeNameStrs);
//                    }
                }
                flag = false;
            }
            // 如果不是基本数据类型则递归遍历
            handleCustomObj(field, classNameList, classFieldNameList, type, refClassNames, refClass);
        }
        refClassNames.remove(simpleName);
        // 防止里层检查了此对象类型后外层不再检查了，所以这里执行完方法的时候需要移除掉此类型，
        // 现在注释掉的原因是：因为前面逻辑做了存在类型的不再嵌套检查字段属性，只检查当前字段属性的判断
//        refClass.remove(type);
    }

    /**
     * 判断字段属性是否有指定注解
     */
    private static boolean checkClassNotAnnotation(Field field) {
        // 判断是否为基础类型
        if (isCustomType(field.getType())) {
            // 判断是否加了指定注解
            FieldEnum annotation = field.getAnnotation(FieldEnum.class);
            return annotation != null;
        } else if (!isReturnBaseType(field.getType())) {
            // 判断是否加了指定注解
            FieldAttribute annotation = field.getAnnotation(FieldAttribute.class);
            return annotation != null;
        }
        return false;
    }

    /**
     * 获取出参类或出参属性上没有加指定注解
     */
    private String getOutParamMissingAnnotation(HandlerMethod handlerMethod) {
        Type genericReturnType = handlerMethod.getMethod().getGenericReturnType();
        Type finalActualTypeFromGenericType = getFinalActualTypeFromGenericType(genericReturnType);
        List<String> classNameList = new ArrayList<>();
        try {
            Class<?> finalActualClazz = Class.forName(finalActualTypeFromGenericType.getTypeName());
            if (isReturnBaseType(finalActualClazz)) {
                // 返回基本类型,没法判定
                return null;
            }
            List<String> classFieldNameList = new ArrayList<>();
            getClassInfo(finalActualClazz, classNameList, classFieldNameList);
            return getResultString(classNameList, classFieldNameList);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    /**
     * 获取接口所有入参是否包含指定的字段属性名称
     */
    private String getInParamCustomFields(HandlerMethod handlerMethod) {
        Parameter[] parameters = handlerMethod.getMethod().getParameters();
        if (parameters.length == 0) {
            return null;
        }
        List<String> fieldNameList = new ArrayList<>();
        Arrays.stream(parameters).forEach(parameter -> {
            Class<?> type = parameter.getType();
            if (isCustomType(type)) {
                if (FIELD_ID.equals(parameter.getName())) {
                    fieldNameList.add(parameter.getName());
                }
                return;
            }
            if (isReturnBaseType(type)) {
                return;
            }
            // 循环获取属性不为基础包装对象类型的成员变量是否包含指定属性名称
            loopObjectField(parameter.getType(), fieldNameList);
        });
        if (CollectionUtils.isEmpty(fieldNameList)) {
            return null;
        }
        return String.join(COMMA, fieldNameList);
    }

    private static void loopObjectField(Class<?> type, List<String> fieldNameList) {
        loopObjectField(type, fieldNameList, new ArrayList<>(), new ArrayList<>());
    }

    /**
     * 递归出/入参嵌套属性对象有指定的字段属性名称
     */
    private static void loopObjectField(Class<?> type, List<String> fieldNameList, List<String> refClassName,
                                        List<Class<?>> refClass) {
        String simpleName = type.getSimpleName();
        refClassName.add(simpleName);
        refClass.add(type);
        Field[] fields = type.getDeclaredFields();
        for (Field field : fields) {
            Class<?> fieldType = field.getType();
            if (isCustomType(fieldType)) {
                if (FIELD_ID.equals(field.getName())) {
                    fieldNameList.add(StringUtils.join(refClassName, "#") + "##" +field.getName());
                }
            } else if (isListType(fieldType)) {
                Class<?> aClass = getParamListType(field);
                if (aClass == null || refClass.contains(aClass) || isReturnBaseType(aClass)) {
                    continue;
                }
                loopObjectField(aClass, fieldNameList, refClassName, refClass);
            } else if (!refClass.contains(fieldType) && !isReturnBaseType(fieldType)) {
                loopObjectField(fieldType, fieldNameList, refClassName, refClass);
            }
        }
        refClassName.remove(simpleName);
    }

    /**
     * 获取接口所有出参是否包含指定字段属性名称
     */
    private String getOutParamCustomFields(HandlerMethod handlerMethod) {
        Type genericReturnType = handlerMethod.getMethod().getGenericReturnType();
        Type finalActualTypeFromGenericType = getFinalActualTypeFromGenericType(genericReturnType);
        List<String> fieldNameList = new ArrayList<>();
        try {
            Class<?> finalActualClazz = Class.forName(finalActualTypeFromGenericType.getTypeName());
            if (isReturnBaseType(finalActualClazz)) {
                // 不是实体对象无法判断
                return null;
            }
            // 循环获取属性不为基础包装对象类型字段是否包含指定属性名称
            loopObjectField(finalActualClazz, fieldNameList);
            if (CollectionUtils.isEmpty(fieldNameList)) {
                return null;
            }
            return String.join(COMMA, fieldNameList);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    /**
     * 获取字段为List集合的泛型类型
     * field通过反射可以获取泛型类型是因为类的class文件中Signature属性记录了泛型签名信息
     * 注意: 由于编译期间会进行泛型类型擦除, 所以局部变量不能获取泛型类型
     */
    private static Class<?> getParamListType(Field field) {
        try {
            if (isListType(field.getType())) {
                Type genericType = field.getGenericType();
                Type fromGenericType = getFinalActualTypeFromGenericType(genericType);
                if (fromGenericType != null) {
                    return Class.forName(fromGenericType.getTypeName());
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    /**
     * 获取字段为数组类型的对象类型
     */
    private static Class<?> getParamArrayType(Class<?> clazz) {
        if (isArrayType(clazz)) {
            return clazz.getComponentType();
        }
        return null;
    }

    /**
     * 调用spring的api获取泛型类型
     * @param field 属性字段信息
     * @param indexes 获取指定位置上的泛型, 例如Map<String, List<List<Integer>>>获取Integer, 则传1, 0, 0
     */
    private static Class<?> getParamListType(Field field, int...indexes) {
        ResolvableType resolvableType = ResolvableType.forField(field);
        if (resolvableType.hasGenerics()) {
            return resolvableType.getGeneric(indexes).resolve();
        }
        return null;
    }

    /**
     * 通过ParameterizedType获取泛型类型: 获取最内层泛型类型
     */
    private static Type getFinalActualTypeFromGenericType(Type type) {
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
     * 通过ParameterizedType获取泛型类型: 获取第一层泛型类型
     */
    private static Type getActualTypeFromGenericType(Type type) {
        if (type instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
            // 取泛型的第一个参数
            return actualTypeArguments[0];
        }
        return null;
    }

    private static boolean isCustomType(Class<?> clazz) {
        // 一般敏感信息字段类型
        return clazz.equals(Integer.class) || clazz.equals(Long.class)
                || clazz.equals(String.class);
    }

    private static boolean isBaseType(Class<?> clazz) {
        return clazz.equals(Integer.class) || clazz.equals(Long.class)
                || clazz.equals(Byte.class) || clazz.equals(Boolean.class)
                || clazz.equals(Character.class) || clazz.equals(String.class)
                || clazz.equals(BigDecimal.class) || clazz.equals(BigInteger.class)
                || clazz.equals(Double.class) || clazz.equals(Float.class)
                || clazz.isEnum() || clazz.equals(Class.class) || clazz.equals(boolean.class)
                || clazz.equals(byte.class) || clazz.equals(long.class)
                || clazz.equals(int.class) || clazz.equals(double.class)
                || clazz.equals(float.class) || clazz.equals(char.class)
                || clazz.equals(short.class) || clazz.equals(Short.class);
    }

    private static boolean isDateType(Class<?> clazz) {
        return clazz.equals(Date.class) || clazz.equals(DateTime.class)
                || clazz.equals(LocalDateTime.class) || clazz.equals(LocalDate.class);
    }

    private static boolean isMapType(Class<?> clazz) {
        return Map.class.isAssignableFrom(clazz);
    }

    private static boolean isListType(Class<?> clazz) {
        return List.class.isAssignableFrom(clazz);
    }

    private static boolean isArrayType(Class<?> clazz) {
        return clazz.isArray();
    }

    private static boolean isVoidType(Class<?> clazz) {
        return clazz.equals(Void.class);
    }

    private static boolean isReturnBaseType(Class<?> clazz) {
        return isBaseType(clazz) || isDateType(clazz) || isVoidType(clazz) || isMapType(clazz);
    }

    /**
     * 判断字段属性名是否包含自定的字符
     */
    private static boolean containsCustomCharFields(String fieldName, List<String> customCharFieldList) {
        if (StrUtil.isBlank(fieldName)) {
            return false;
        }
        fieldName = fieldName.toLowerCase();
        for (String customCharField : customCharFieldList) {
            if (fieldName.contains(customCharField)) {
                return true;
            }
        }
        return false;
    }

}

@Getter
@Setter
@ToString
class RequestMappingDTO {

    @ExcelProperty(value = "Controller名称", index = 0)
    @ColumnWidth(24)
    @ApiModelProperty("Controller名称")
    private String controller;

    @ExcelProperty(value = "urlResName", index = 1)
    @ColumnWidth(29)
    @ApiModelProperty("接口名称")
    private String urlResName;

    @ExcelProperty(value = "url映射", index = 2)
    @ColumnWidth(39)
    @ApiModelProperty("url映射")
    private String urlMapping;

    @ExcelProperty(value = "入参包含指定字符的字段不带注解", index = 3)
    @ColumnWidth(35)
    @ApiModelProperty("入参包含指定字符的字段不带注解")
    private String inParamIncludeFieldsNotAnnotation;

    @ExcelProperty(value = "出参包含指定字符的字段不带注解", index = 4)
    @ColumnWidth(42)
    @ApiModelProperty("出参包含指定字符的字段不带注解")
    private String outParamIncludeFieldsNotAnnotation;

    @ExcelProperty(value = "入参类或自定义对象属性上遗漏注解", index = 5)
    @ColumnWidth(33)
    @ApiModelProperty("入参类或自定义对象属性上遗漏注解")
    private String inParamMissingAnnotation;

    @ExcelProperty(value = "出参类或自定义对象属性上遗漏注解", index = 6)
    @ColumnWidth(33)
    @ApiModelProperty("出参类或自定义对象属性上遗漏注解")
    private String outParamMissingAnnotation;

    @ExcelProperty(value = "入参包含指定字段", index = 7)
    @ColumnWidth(8)
    @ApiModelProperty("入参包含指定字段")
    private String inParamCustomFields;

    @ExcelProperty(value = "出参包含指定字段", index = 8)
    @ColumnWidth(8)
    @ApiModelProperty("出参包含指定字段")
    private String outParamCustomFields;

    @ExcelProperty(value = "入参字段信息", index = 9)
    @ColumnWidth(17)
    @ApiModelProperty("入参字段信息")
    private String inParamFields;

    @ExcelProperty(value = "出参字段信息", index = 10)
    @ColumnWidth(18)
    @ApiModelProperty("出参字段信息")
    private String outParamFields;
}

/**
 * 此枚举作用在基础类型的包装类型字段上
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@interface FieldEnum {}

/**
 * 此枚举作用在自定义对象类型的字段上
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@interface FieldAttribute {}

/**
 * 此枚举作用在自定义对象类型的类上
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@interface FieldClass {}
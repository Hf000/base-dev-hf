package org.hf.boot.springboot.error.handler;

import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson2.JSON;
import com.github.pagehelper.PageInfo;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.hf.boot.springboot.pojo.dto.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p> 全局异常处理器 </p>
 * //@RestControllerAdvice  // 创建全局异常处理器 @ControllerAdvice+@ResponseBody的组合注解;
 * @author hufei
 * @date 2022/8/12 23:18
*/
@RestControllerAdvice
public class GlobalExceptionHandler implements ResponseBodyAdvice<Object> {

    /**
     * 创建异常处理器
     * @return 返回结果
     */
    @ExceptionHandler(Exception.class)
    public String exceptionHandlerMethod(Exception exception) {
        exception.printStackTrace();
        return exception.getMessage();
    }

    /**
     * 创建异常处理器
     * @return 返回结果
     */
    @ExceptionHandler(RuntimeException.class)
    public String runtimeExceptionHandlerMethod(RuntimeException exception) {
        exception.printStackTrace();
        return exception.getMessage();
    }

    /*****************************以上是全局异常捕获配置,以下是对接口出参返回响应拦截处理*************************************/

    /**
     * 是否开启响应处理开关
     */
    @Value("${response.body.handle.enable:false}")
    private Boolean responseBodyHandleEnable;

    @Override
    public boolean supports(@NonNull MethodParameter returnType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        // 如果不需要进行封装, 可以添加一些校验的手段, 比如添加标记排除的注解
        if (!responseBodyHandleEnable) {
            return false;
        }
        return true;
    }

    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object body, @NonNull MethodParameter returnType, @NonNull MediaType selectedContentType, @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType, @NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response) {
        // 提供一定的灵活度, 如果body已经被包装, 就不需要进行包装了
        if (body instanceof String) {
            // 如果是string类型不能直接转换成其他类型, 因为spring在做类型转换的时候会识别成string
//            ObjectMapper om = new ObjectMapper();
//            return om.writeValueAsString(new Result<>(false, StatusCode.ERROR, "系统错误"));
            // 在配置类org.hf.boot.springboot.config.WebConfig中的configureMessageConverters方法处理转换器
            return Result.fail(body);
        } else {
            // 注意: 这里不能统一包装,需要根据已识别的返回类型做定制化封装,如果统一包装可能会出现问题,例如:会导致swagger无法使用,因为将swagger返回类型进行了转换导致无法识别
            if (body instanceof Result) {
                // 处理请求响应
                responseBodyHandle(request, body);
            }
            return body;
        }
    }

    @SuppressWarnings("all")
    private void responseBodyHandle(ServerHttpRequest request, Object body) {
        String path = request.getURI().getPath();
        // 根据请求的接口路径查询是否需要处理的出参, 这个信息可以通过保存在指定的表中 getResponseBodyByUri();
        // 这里先指定一个map, key为请求uri, value为需要处理的字段名称
        Map<String, List<String>> sensitiveMap = new HashMap<>();
        if (CollectionUtils.isEmpty(sensitiveMap.get(path))) {
            return;
        }
        List<String> sensitiveFields = sensitiveMap.get(path);
        handleResponseData(body, sensitiveFields);
    }

    /**
     * 处理接口响应数据内容,将所有敏感字段置为null
     *
     * @param obj           返回数据
     * @param sensitiveList 敏感字段列表
     */
    public static void handleResponseData(Object obj, List<String> sensitiveList) {
        if (Objects.isNull(obj) || CollectionUtils.isEmpty(sensitiveList)) {
            return;
        }
        Result<?> result = JSON.parseObject(JSON.toJSONString(obj), Result.class);
        if (result == null || !Boolean.TRUE.equals(result.isFlag()) || result.getData() == null) {
            return;
        }
        Object data = ((Result<?>) obj).getData();
        if (data instanceof Collection) {
            if (CollectionUtils.isEmpty((Collection<?>) data)) {
                return;
            }
            // Java集合类型
            desensitiseCollectionField((Collection<?>) data, sensitiveList);
        } else if (data instanceof PageInfo) {
            List<?> list = ((PageInfo<?>) data).getList();
            if (CollectionUtils.isEmpty(list)) {
                return;
            }
            // PageInfo中List类型
            desensitiseCollectionField(list, sensitiveList);
        } else {
            // 普通对象类型
            desensitiseObjectField(data, sensitiveList);
        }
    }

    private static void desensitiseCollectionField(Collection<?> collection, List<String> sensitiveList) {
        // Java集合类型
        collection.forEach(item -> desensitiseObjectField(item, sensitiveList));
    }

    private static void desensitiseObjectField(Object obj, List<String> sensitiveList) {
        if (isReturnBaseType(obj.getClass())) {
            return;
        }
        // 取集合中的元素脱敏一次
        List<Object> mismatchedList = desensitiseField(obj, sensitiveList);
        // 目前是集合最多扫描三次, 对象两次, 可以改写成循环处理, 但是需要注意嵌套对象的类型问题,避免死循环
        mismatchedList.forEach(item -> {
            if (item instanceof Collection) {
                if (CollectionUtils.isEmpty((Collection<?>) item)) {
                    return;
                }
                ((Collection<?>) item).forEach(secItem -> desensitiseField(secItem, sensitiveList));
            } else {
                desensitiseField(item, sensitiveList);
            }
        });
    }

    /**
     * 处理字段脱敏
     *
     * @param obj           待脱敏对象
     * @param sensitiveList 脱敏字段
     * @return  返回所有不处理的对象
     */
    @SneakyThrows
    private static List<Object> desensitiseField(Object obj, List<String> sensitiveList) {
        if (obj == null) {
            return new ArrayList<>();
        }
        Class<?> dataClass = obj.getClass();
        Field[] declaredFields = dataClass.getDeclaredFields();
        List<Object> mismatchFields = new ArrayList<>();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            if (sensitiveList.contains(fieldName)) {
                field.set(obj, null);
            } else {
                Class<?> fieldType = field.getType();
                if (isReturnBaseType(fieldType)) {
                    continue;
                }
                // 将所有未脱敏的字段不为空的对象返回
                Object secObj = field.get(obj);
                if (secObj != null) {
                    mismatchFields.add(secObj);
                }
            }
        }
        return mismatchFields;
    }

    private static boolean isReturnBaseType(Class<?> clazz) {
        return isBaseType(clazz) || isDateType(clazz) || isVoidType(clazz) || isMapType(clazz);
    }

    private static boolean isBaseType(Class<?> clazz) {
        return clazz.equals(Integer.class) || clazz.equals(Long.class)
                || clazz.equals(Byte.class) || clazz.equals(Boolean.class)
                || clazz.equals(Character.class) || clazz.equals(String.class)
                || clazz.equals(BigDecimal.class) || clazz.equals(BigInteger.class)
                || clazz.equals(Double.class) || clazz.equals(Float.class)
                || clazz.isEnum() || clazz.isArray() || clazz.equals(Class.class) || clazz.equals(boolean.class)
                || clazz.equals(byte.class) || clazz.equals(long.class)
                || clazz.equals(int.class) || clazz.equals(double.class)
                || clazz.equals(float.class) || clazz.equals(char.class)
                || clazz.equals(short.class) || clazz.equals(Short.class);
    }

    private static boolean isDateType(Class<?> clazz) {
        return clazz.equals(Date.class) || clazz.equals(DateTime.class)
                || clazz.equals(LocalDateTime.class) || clazz.equals(LocalDate.class);
    }

    private static boolean isVoidType(Class<?> clazz) {
        return clazz.equals(Void.class);
    }

    private static boolean isMapType(Class<?> clazz) {
        return Map.class.isAssignableFrom(clazz);
    }

}

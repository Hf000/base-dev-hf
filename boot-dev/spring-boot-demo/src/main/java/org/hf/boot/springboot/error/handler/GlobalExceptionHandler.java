package org.hf.boot.springboot.error.handler;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

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
    public String exceptionHandlerMethod() {
        return "error";
    }

    /**
     * 创建异常处理器
     * @return 返回结果
     */
    @ExceptionHandler(RuntimeException.class)
    public String runtimeExceptionHandlerMethod() {
        return "runtimeException error";
    }

    @Override
    public boolean supports(@NonNull MethodParameter returnType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        // 如果不需要进行封装, 可以添加一些校验的手段, 比如添加标记排除的注解
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, @NonNull MethodParameter returnType, @NonNull MediaType selectedContentType, @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType, @NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response) {
        // 提供一定的灵活度, 如果body已经被包装, 就不需要进行包装了
        if (body instanceof String) {
            return "字符串拦截了" + body;
        }
        return body;
    }
}

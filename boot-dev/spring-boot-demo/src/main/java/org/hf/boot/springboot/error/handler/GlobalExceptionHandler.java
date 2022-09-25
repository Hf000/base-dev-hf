package org.hf.boot.springboot.error.handler;

import lombok.SneakyThrows;
import org.hf.boot.springboot.pojo.dto.Result;
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

    @Override
    public boolean supports(@NonNull MethodParameter returnType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        // 如果不需要进行封装, 可以添加一些校验的手段, 比如添加标记排除的注解
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
        }
        if (body instanceof Result) {
            return body;
        }
        return Result.success(body);
    }
}

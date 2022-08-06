package org.hf.boot.springboot.error.handler;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author:hufei
 * @CreateTime:2020-09-09
 * @Description:全局异常处理器
 */
@RestControllerAdvice                   //创建全局异常处理器 @ControllerAdvice+@ResponseBody的组合注解;
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)           //创建异常处理器
    public String exceptionHandlerMethod() {
        return "error";
    }

    @ExceptionHandler(RuntimeException.class)           //创建异常处理器
    public String runtimeExceptionHandlerMethod() {
        return "runtimeException error";
    }

}

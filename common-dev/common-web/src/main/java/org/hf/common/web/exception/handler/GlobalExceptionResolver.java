package org.hf.common.web.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.hf.common.publi.enums.ExceptionEnum;
import org.hf.common.publi.exception.CheckedException;
import org.hf.common.publi.exception.UnCheckedException;
import org.hf.common.web.exception.customize.WebException;
import org.hf.common.web.pojo.vo.ResponseVO;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * <p> 全局异常捕获整理 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2021/7/25 19:02
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionResolver {

    /**
     * 捕获throwable异常 //创建异常处理器
     * @param e Throwable
     * @return ResponseVO<Void>
     */
    @ExceptionHandler(Throwable.class)
    public ResponseVO<Void> throwableHandlerMethod(Throwable e) {
        log.error("get a Throwable", e);
        ResponseVO<Void> responseVO = new ResponseVO<>();
        responseVO.setResponseCode(ExceptionEnum.SYSTEMERROR.getCode());
        responseVO.setResponseMsg(ExceptionEnum.SYSTEMERROR.getMsg());
        return responseVO;
    }

    /**
     * 捕获exception异常 //创建异常处理器
     * @param e Exception
     * @return ResponseVO<Void>
     */
    @ExceptionHandler(Exception.class)
    public ResponseVO<Void> exceptionHandlerMethod(Exception e) {
        log.error("get a Exception", e);
        ResponseVO<Void> responseVO = new ResponseVO<>();
        responseVO.setResponseCode(ExceptionEnum.SYSTEMERROR.getCode());
        responseVO.setResponseMsg(ExceptionEnum.SYSTEMERROR.getMsg());
        return responseVO;
    }

    /**
     * 捕获RuntimeException异常 //创建异常处理器
     * @param e RuntimeException
     * @return ResponseVO<Void>
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseVO<Void> runtimeExceptionHandlerMethod(RuntimeException e) {
        log.error("get a RuntimeException", e);
        ResponseVO<Void> responseVO = new ResponseVO<>();
        responseVO.setResponseCode(ExceptionEnum.SYSTEMERROR.getCode());
        responseVO.setResponseMsg(ExceptionEnum.SYSTEMERROR.getMsg());
        return responseVO;
    }

    /**
     * 捕获CheckedException异常 //创建异常处理器
     * @param e CheckedException
     * @return ResponseVO<Void>
     */
    @ExceptionHandler(CheckedException.class)
    public ResponseVO<Void> checkedExceptionHandlerMethod(CheckedException e) {
        log.error("get a CheckedException", e);
        ResponseVO<Void> responseVO = new ResponseVO<>();
        responseVO.setResponseCode(ExceptionEnum.SYSTEMERROR.getCode());
        responseVO.setResponseMsg(ExceptionEnum.SYSTEMERROR.getMsg());
        return responseVO;
    }

    /**
     * 捕获UnCheckedException异常 //创建异常处理器
     * @param e UnCheckedException
     * @return ResponseVO<Void>
     */
    @ExceptionHandler(UnCheckedException.class)
    public ResponseVO<Void> unCheckedExceptionHandlerMethod(UnCheckedException e) {
        log.error("get a UnCheckedException", e);
        ResponseVO<Void> responseVO = new ResponseVO<>();
        responseVO.setResponseCode(ExceptionEnum.SYSTEMERROR.getCode());
        responseVO.setResponseMsg(ExceptionEnum.SYSTEMERROR.getMsg());
        return responseVO;
    }

    /**
     * 捕获WebException异常 //创建异常处理器
     * @param e WebException
     * @return ResponseVO<Void>
     */
    @ExceptionHandler(WebException.class)
    public ResponseVO<Void> webExceptionHandlerMethod(WebException e) {
        log.error("get a WebException", e);
        ResponseVO<Void> responseVO = new ResponseVO<>();
        responseVO.setResponseCode(ExceptionEnum.SYSTEMERROR.getCode());
        responseVO.setResponseMsg(ExceptionEnum.SYSTEMERROR.getMsg());
        return responseVO;
    }

}

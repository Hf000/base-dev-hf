package org.hf.common.web.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.hf.common.publi.enums.ExceptionEnum;
import org.hf.common.publi.exception.BusinessException;
import org.hf.common.publi.exception.UnCheckedException;
import org.hf.common.web.exception.customize.WebException;
import org.hf.common.web.pojo.vo.ResponseVO;
import org.hf.common.web.utils.CustomValidationUtil;
import org.hf.common.web.utils.ResponseUtil;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.UnexpectedTypeException;
import java.net.BindException;
import java.sql.SQLException;

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
     * 针对JSR303系列校验注解使用错误的异常捕捉
     * @param exception UnexpectedTypeException
     * @return ResponseVO<Void>
     */
    @ExceptionHandler({UnexpectedTypeException.class})
    public ResponseVO<Void> unexpectedTypeExceptionHandler(UnexpectedTypeException exception) {
        log.error("get a UnexpectedTypeException", exception);
        return ResponseUtil.error(ExceptionEnum.SYSTEMERROR.getCode(), exception.getMessage());
    }

    /**
     * 请求参数为空
     * @param exception MissingServletRequestParameterException
     * @return ResponseVO<Void>
     */
    @ExceptionHandler({MissingServletRequestParameterException.class})
    public ResponseVO<Void> missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException exception) {
        log.error("get a MissingServletRequestParameterException", exception);
        return ResponseUtil.error(ExceptionEnum.SYSTEMERROR.getCode(), exception.getMessage());
    }

    /**
     * 请求体为空或者无法解析 异常
     * @param exception HttpMessageNotReadableException
     * @return ResponseVO<Void>
     */
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseVO<Void> httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException exception, HttpServletRequest request) {
        log.info("请求体为空或者无法解析, url;{}", request.getRequestURL());
        log.error("get a HttpMessageNotReadableException", exception);
        return ResponseUtil.error(ExceptionEnum.SYSTEMERROR.getCode(), exception.getMessage());
    }

    /**
     * 捕获http请求方法不支持 异常
     * @param exception HttpRequestMethodNotSupportedException
     * @return ResponseVO<Void>
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResponseVO<Void> httpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException exception, HttpServletRequest request) {
        log.info("HTTP请求不支持, url:{}", request.getRequestURL());
        log.error("get a HttpRequestMethodNotSupportedException", exception);
        return ResponseUtil.error(ExceptionEnum.SYSTEMERROR.getCode(), exception.getMessage());
    }

    /**
     * 参数绑定异常
     * @param exception BindException
     * @return ResponseVO<Void>
     */
    @ExceptionHandler({BindException.class})
    public ResponseVO<Void> bindExceptionHandler(BindException exception) {
        log.error("get a BindException", exception);
        return ResponseUtil.error(ExceptionEnum.SYSTEMERROR.getCode(), exception.getMessage());
    }

    /**
     * 方法参数校验异常, 结合@Validated和@Valid使用
     * @param exception MethodArgumentNotValidException
     * @return ResponseVO<Void>
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseVO<Void> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception) {
        log.error("get a MethodArgumentNotValidException", exception);
        return ResponseUtil.error(ExceptionEnum.SYSTEMERROR.getCode(), CustomValidationUtil.getValidatorResultDesc(exception.getBindingResult()));
    }

    /**
     * 媒介类型错误异常
     * @param exception HttpMediaTypeNotSupportedException
     * @return ResponseVO<Void>
     */
    @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
    public ResponseVO<Void> httpMediaTypeNotSupportedExceptionHandler(HttpMediaTypeNotSupportedException exception) {
        log.error("get a HttpMediaTypeNotSupportedException", exception);
        return ResponseUtil.error(ExceptionEnum.SYSTEMERROR.getCode(), exception.getMessage());
    }

    /**
     * 捕获WebException异常 //创建异常处理器
     * @param e WebException
     * @return ResponseVO<Void>
     */
    @ExceptionHandler(WebException.class)
    public ResponseVO<Void> webExceptionHandler(WebException e) {
        log.error("get a WebException", e);
        ResponseVO<Void> responseVO = new ResponseVO<>();
        responseVO.setResponseCode(e.getCode());
        responseVO.setResponseMsg(e.getMsg());
        return responseVO;
    }

    /**
     * 捕获BusinessException异常
     * @param e BusinessException
     * @return ResponseVO<Void>
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseVO<Void> businessExceptionHandler(BusinessException e) {
        log.error("get a BusinessException", e);
        ResponseVO<Void> responseVO = new ResponseVO<>();
        responseVO.setResponseCode(e.getCode());
        responseVO.setResponseMsg(e.getMsg());
        return responseVO;
    }

    /**
     * sql语法错误异常
     * @param exception BadSqlGrammarException
     * @return ResponseVO<Void>
     */
    @ExceptionHandler({BadSqlGrammarException.class})
    public ResponseVO<Void> badSqlGrammarExceptionHandler(BadSqlGrammarException exception) {
        log.error("get a BadSqlGrammarException", exception);
        return ResponseUtil.error(ExceptionEnum.SYSTEMERROR.getCode(), exception.getMessage());
    }

    /**
     * sql执行异常
     * @param exception SQLException
     * @return ResponseVO<Void>
     */
    @ExceptionHandler({SQLException.class})
    public ResponseVO<Void> sqlExceptionHandler(SQLException exception) {
        log.error("get a SQLException", exception);
        return ResponseUtil.error(ExceptionEnum.SYSTEMERROR.getCode(), exception.getMessage());
    }

    /**
     * 捕获UnCheckedException异常 //创建异常处理器
     * @param e UnCheckedException
     * @return ResponseVO<Void>
     */
    @ExceptionHandler(UnCheckedException.class)
    public ResponseVO<Void> unCheckedExceptionHandler(UnCheckedException e) {
        log.error("get a UnCheckedException", e);
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
    public ResponseVO<Void> runtimeExceptionHandler(RuntimeException e) {
        log.error("get a RuntimeException", e);
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
    public ResponseVO<Void> exceptionHandler(Exception e) {
        log.error("get a Exception", e);
        ResponseVO<Void> responseVO = new ResponseVO<>();
        responseVO.setResponseCode(ExceptionEnum.SYSTEMERROR.getCode());
        responseVO.setResponseMsg(ExceptionEnum.SYSTEMERROR.getMsg());
        return responseVO;
    }

    /**
     * 捕获throwable异常 //创建异常处理器
     * @param e Throwable
     * @return ResponseVO<Void>
     */
    @ExceptionHandler(Throwable.class)
    public ResponseVO<Void> throwableHandler(Throwable e) {
        log.error("get a Throwable", e);
        ResponseVO<Void> responseVO = new ResponseVO<>();
        responseVO.setResponseCode(ExceptionEnum.SYSTEMERROR.getCode());
        responseVO.setResponseMsg(ExceptionEnum.SYSTEMERROR.getMsg());
        return responseVO;
    }

}

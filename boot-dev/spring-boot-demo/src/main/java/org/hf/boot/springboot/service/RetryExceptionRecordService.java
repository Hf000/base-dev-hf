package org.hf.boot.springboot.service;

import org.hf.boot.springboot.pojo.dto.InvokeMethodReq;
import org.hf.boot.springboot.pojo.dto.RetryExceptionReq;
import org.hf.boot.springboot.pojo.entity.RetryExceptionRecord;

/**
 * <p> 重试异常接口 </p >
 * 自定义重试异常实现 - 3
 * @author HUFEI
 * @date 2023-06-07
 **/
public interface RetryExceptionRecordService {

    /**
     * 新增异常记录
     * @param exceptionEntity 异常信息
     */
    void saveExceptionRecord(RetryExceptionRecord exceptionEntity);

    /**
     * 重试异常记录
     * @param req 请求入参
     */
    void retryExceptionRecord(RetryExceptionReq req);

    /**
     * 调用service方法
     * @param req 入参
     * @return Object
     */
    Object invokeServiceMethod(InvokeMethodReq req);

    String springExceptionRetry(Integer number);
}
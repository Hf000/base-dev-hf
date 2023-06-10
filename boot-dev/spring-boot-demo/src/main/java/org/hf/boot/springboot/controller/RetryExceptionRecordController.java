package org.hf.boot.springboot.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hf.boot.springboot.pojo.dto.Result;
import org.hf.boot.springboot.pojo.dto.RetryExceptionReq;
import org.hf.boot.springboot.service.RetryExceptionRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p> 异常重试 </p >
 * @author hufei
 * @date 2022/8/13 9:02
 */
@Api(tags = "异常重试")
@RestController
@RequestMapping("/retry")
public class RetryExceptionRecordController {

    @Autowired
    private RetryExceptionRecordService retryExceptionRecordService;

    @ApiOperation(value = "重试异常记录")
    @PostMapping("/exceptionRecord")
    public Result<Void> retryExceptionRecord(@RequestBody RetryExceptionReq req) {
        retryExceptionRecordService.retryExceptionRecord(req);
        return new Result<>();
    }
}
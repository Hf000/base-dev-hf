package org.hf.boot.springboot.controller;

import io.swagger.annotations.ApiOperation;
import org.hf.boot.springboot.pojo.dto.Result;
import org.hf.boot.springboot.service.StreamingResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * 流失响应接口示例
 *
 * @author HF
 */
@RestController
@RequestMapping("/stream")
public class StreamingResponseController {

    @Autowired
    private StreamingResponseService streamingResponseService;

    @ApiOperation(value = "流式输出")
    @PostMapping(value = "/streamingResponse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamingResponse() {
        return streamingResponseService.streamingResponse();
    }

    @ApiOperation(value = "流式拼接后出")
    @PostMapping("/blockingResponse")
    public Result<String> blockingResponse() {
        return Result.success(streamingResponseService.blockingResponse());
    }

}

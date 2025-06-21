package org.hf.boot.springboot.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface StreamingResponseService {

    /**
     * 流式输出接口
     * @return  通过SSE进行流式响应
     */
    SseEmitter streamingResponse();

    /**
     * 流式拼接后输出接口
     * @return  拼接好的结果
     */
    String blockingResponse();
}
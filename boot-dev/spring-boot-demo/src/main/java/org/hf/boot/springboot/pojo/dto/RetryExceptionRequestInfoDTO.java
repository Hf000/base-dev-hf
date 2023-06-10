package org.hf.boot.springboot.pojo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

/**
 * <p> 请求信息实体 </p >
 * 自定义重试异常实现 - 7
 * @author HUFEI
 * @date 2023-06-07
 **/
@Getter
@Setter
@ToString
public class RetryExceptionRequestInfoDTO {

    /**
     * 请求url
     */
    private String requestUrl;

    /**
     * 请求方式GET,POST
     */
    private String requestType;

    /**
     * 请求body参数
     */
    private String requestBodyParams;

    /**
     * 请求路径参数
     */
    private String requestPathParams;

    /**
     * 请求headers
     */
    private Map<String, Object> requestHeaders;

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 方法参数
     */
    private Map<String, Object> methodParams;
}
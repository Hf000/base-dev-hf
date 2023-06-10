package org.hf.boot.springboot.pojo.dto;

import lombok.Data;
import org.hf.boot.springboot.constants.RetryTypeEnum;

import java.io.Serializable;
import java.util.List;

/**
 * @author HUFEI
 * 自定义重试异常实现 - 6
 */
@Data
public class RetryExceptionReq implements Serializable {

    private static final long serialVersionUID = 5061460930791609106L;
    /**
     * 重试异常记录id
     */
    private Integer id;

    /**
     * 请求完整地址url
     */
    private String retryUrl;

    /**
     * 是调用方法, 还是通过http访问 "METHOD", "URL" RetryTypeEnum
     */
    private RetryTypeEnum retryType;

    /**
     * 请求方式 GET POST
     */
    private String requestType;

    /**
     * 请求cookie
     */
    List<KeyValueInfo> cookies;

    /**
     * 请求header
     */
    List<KeyValueInfo> headers;

    /**
     * 请求入参
     */
    List<KeyValueInfo> params;

    @Data
    public static class KeyValueInfo {
        private String key;
        private Object value;
    }
}
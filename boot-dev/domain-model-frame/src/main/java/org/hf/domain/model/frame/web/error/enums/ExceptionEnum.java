package org.hf.domain.model.frame.web.error.enums;

import org.hf.common.publi.interfac.StatusCode;

/**
 * <p> 异常响应枚举类 </p >
 */
public enum ExceptionEnum implements StatusCode {
    /**
     * 响应枚举类
     */
    CRON_ERROR("400001","系统异常"),
    REQ_ERROR("400002","请求异常"),
    NOT_FOUND("400004", "没找到请求"),
    ;

    ExceptionEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private final String code;
    private final String msg;

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMsg() {
        return this.msg;
    }
}
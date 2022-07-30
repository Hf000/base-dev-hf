package org.hf.common.publi.enums;

import org.hf.common.publi.interfac.StatusCode;

/**
 * <p> 错误异常响应枚举类 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2021/7/25 18:38
 */
public enum ExceptionEnum implements StatusCode {
    /**
     * 异常枚举
     */
    SYSTEMERROR("400000", "系统异常"),
    CHECKERROR("400001", "检查异常"),
    ;

    private final String code;
    private final String msg;

    ExceptionEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMsg() {
        return this.msg;
    }
}

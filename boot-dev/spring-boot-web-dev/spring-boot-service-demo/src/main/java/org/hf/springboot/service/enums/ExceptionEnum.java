package org.hf.springboot.service.enums;

import org.hf.common.publi.interfac.StatusCode;

/**
 * <p> 异常响应枚举类 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2021/7/25 19:44
 */
public enum ExceptionEnum implements StatusCode {
    /**
     * 响应枚举类
     */
    EXECUTE_JOB_ERROR("400001","该定时任务不存在"),
    JOB_CRON_ERROR("400002","定时任务表达式格式不正确"),
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

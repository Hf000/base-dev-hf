package org.hf.common.config.enums;

import org.hf.common.publi.interfac.StatusCode;

/**
 * <p> 响应枚举类 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2021/7/25 19:44
 */
public enum ConfigExceptionEnum implements StatusCode {
    /**
     * 响应枚举类
     */
    CRON_ERROR("400001","cron表达式异常"),
    ;

    ConfigExceptionEnum(String code, String msg) {
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

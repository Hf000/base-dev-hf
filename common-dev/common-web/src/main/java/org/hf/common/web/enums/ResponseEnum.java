package org.hf.common.web.enums;

import org.hf.common.publi.interfac.StatusCode;

/**
 * <p> 响应枚举类 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2021/7/25 19:44
 */
public enum ResponseEnum implements StatusCode {
    /**
     * 响应枚举类
     */
    SUCCESS("100001","成功"),
    FAIL("100002","失败"),
    ;

    private ResponseEnum(String code, String msg) {
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

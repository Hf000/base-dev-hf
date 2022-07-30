package org.hf.common.publi.exception;

import org.hf.common.publi.enums.ExceptionEnum;
import org.hf.common.publi.interfac.StatusCode;

/**
 * <p> 自定义可检查异常父类 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2021/7/25 18:33
 */
public class CheckedException extends Exception {

    private static final long serialVersionUID = -8726161704595220578L;
    protected String code;
    protected String msg;

    protected CheckedException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    protected CheckedException(StatusCode statusCode) {
        this.code = statusCode.getCode();
        this.msg = statusCode.getMsg();
    }

    protected CheckedException(String msg) {
        this.code = ExceptionEnum.SYSTEMERROR.getCode();
        this.msg = msg;
    }

    protected CheckedException(StatusCode statusCode, String msg) {
        this.code = statusCode.getCode();
        this.msg = msg;
    }

    protected CheckedException() {
        this.code = ExceptionEnum.SYSTEMERROR.getCode();
        this.msg = ExceptionEnum.SYSTEMERROR.getMsg();
    }

    public String getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

}

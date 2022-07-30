package org.hf.common.publi.exception;


import org.hf.common.publi.enums.ExceptionEnum;
import org.hf.common.publi.interfac.StatusCode;

/**
 * <p> 自定义运行时异常父类 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2021/7/25 18:33
 */
public class UnCheckedException extends RuntimeException {

    private static final long serialVersionUID = 1785513248562676275L;
    protected String code;
    protected String msg;

    protected UnCheckedException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    protected UnCheckedException(StatusCode statusCode) {
        this.code = statusCode.getCode();
        this.msg = statusCode.getMsg();
    }

    protected UnCheckedException(String msg) {
        this.code = ExceptionEnum.SYSTEMERROR.getCode();
        this.msg = msg;
    }

    protected UnCheckedException(StatusCode statusCode, String msg) {
        this.code = statusCode.getCode();
        this.msg = msg;
    }

    protected UnCheckedException() {
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

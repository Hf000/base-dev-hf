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

    public UnCheckedException(String code, String msg) {
        super();
        this.code = code;
        this.msg = msg;
    }

    public UnCheckedException(String code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public UnCheckedException(StatusCode statusCode) {
        super();
        this.code = statusCode.getCode();
        this.msg = statusCode.getMsg();
    }

    public UnCheckedException(StatusCode statusCode, Throwable cause) {
        super(cause);
        this.code = statusCode.getCode();
        this.msg = statusCode.getMsg();
    }

    public UnCheckedException(String msg) {
        super();
        this.code = ExceptionEnum.SYSTEMERROR.getCode();
        this.msg = msg;
    }

    public UnCheckedException(StatusCode statusCode, String msg) {
        super();
        this.code = statusCode.getCode();
        this.msg = msg;
    }

    public UnCheckedException() {
        super();
        this.code = ExceptionEnum.SYSTEMERROR.getCode();
        this.msg = ExceptionEnum.SYSTEMERROR.getMsg();
    }

    public UnCheckedException(Throwable cause) {
        super(cause);
        this.code = ExceptionEnum.SYSTEMERROR.getCode();
    }

    public String getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

}

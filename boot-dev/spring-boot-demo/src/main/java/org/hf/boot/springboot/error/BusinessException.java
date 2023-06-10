package org.hf.boot.springboot.error;

/**
 * <p> 业务异常类 </p >
 *
 * @author hufei
 * @version 1.0.0
 * @date 2022/8/13 19:18
 */
public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 1785513248562676275L;
    protected String code;
    protected String msg;

    public BusinessException(String code, String msg) {
        super();
        this.code = code;
        this.msg = msg;
    }

    public BusinessException(String code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public BusinessException(ExceptionEnum exceptionEnum) {
        super();
        this.code = exceptionEnum.getCode();
        this.msg = exceptionEnum.getMsg();
    }

    public BusinessException(ExceptionEnum exceptionEnum, Throwable cause) {
        super(cause);
        this.code = exceptionEnum.getCode();
        this.msg = exceptionEnum.getMsg();
    }

    public BusinessException(String msg) {
        super();
        this.code = ExceptionEnum.SYSTEMERROR.getCode();
        this.msg = msg;
    }

    public BusinessException(ExceptionEnum exceptionEnum, String msg) {
        super();
        this.code = exceptionEnum.getCode();
        this.msg = msg;
    }

    public BusinessException() {
        super();
        this.code = ExceptionEnum.SYSTEMERROR.getCode();
        this.msg = ExceptionEnum.SYSTEMERROR.getMsg();
    }

    public BusinessException(Throwable cause) {
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

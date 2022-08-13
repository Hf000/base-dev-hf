package org.hf.common.publi.exception;

import org.hf.common.publi.interfac.StatusCode;

/**
 * <p> 业务异常类 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2022/8/13 19:18
 */
public class BusinessException extends UnCheckedException {
    private static final long serialVersionUID = 2639084438040300726L;

    public BusinessException(String code, String msg) {
        super(code, msg);
    }

    public BusinessException(String code, Throwable cause) {
        super(code, cause);
    }

    public BusinessException(StatusCode statusCode) {
        super(statusCode);
    }

    public BusinessException(StatusCode statusCode, Throwable cause) {
        super(statusCode, cause);
    }

    public BusinessException(String msg) {
        super(msg);
    }

    public BusinessException(StatusCode statusCode, String msg) {
        super(statusCode, msg);
    }

    public BusinessException() {
        super();
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }
}

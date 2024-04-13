package org.hf.domain.model.frame.web.error.exception;

import org.hf.common.publi.interfac.StatusCode;
import org.hf.common.web.exception.customize.WebException;
import org.hf.domain.model.frame.web.error.enums.ExceptionEnum;

/**
 * <p> 自定义异常 </p >
 */
public class CustomWebException extends WebException {

    private static final long serialVersionUID = -996970154145064021L;

    public CustomWebException(StatusCode statusCode) {
        super(statusCode);
    }

    public CustomWebException(String code, String msg) {
        super(code, msg);
    }

    public CustomWebException(String msg) {
        super(ExceptionEnum.REQ_ERROR.getCode(), msg);
    }
}
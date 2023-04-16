package org.hf.springboot.web.exception;

import org.hf.common.publi.interfac.StatusCode;
import org.hf.common.web.exception.customize.WebException;
import org.hf.springboot.web.enums.ExceptionEnum;

/**
 * <p> 自定义异常 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2021/10/9 10:28
 */
public class CustomWebException extends WebException {
    private static final long serialVersionUID = 6093873036324529919L;

    public CustomWebException(StatusCode statusCode) {
        super(statusCode);
    }

    public CustomWebException(String code, String msg) {
        super(code, msg);
    }

    public CustomWebException(String msg) {
        super(ExceptionEnum.CRON_ERROR.getCode(), msg);
    }
}

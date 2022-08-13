package org.hf.common.web.exception.customize;

import org.hf.common.publi.enums.ExceptionEnum;
import org.hf.common.publi.exception.UnCheckedException;
import org.hf.common.publi.interfac.StatusCode;

import java.io.UncheckedIOException;
import java.util.List;

/**
 * <p> 自定义web层异常父类 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2021/7/25 18:33
 */
public class WebException extends UnCheckedException {

    private static final long serialVersionUID = -5098852202248205572L;
    protected List<?> dataList;

    protected WebException(String code, String msg, List<?> dataList) {
        super(code, msg);
        this.dataList = dataList;
    }

    protected WebException(StatusCode statusCode, List<?> dataList) {
        super(statusCode);
        this.dataList = dataList;
    }

    protected WebException(StatusCode statusCode, List<?> dataList, Throwable cause) {
        super(statusCode, cause);
        this.dataList = dataList;
    }

    protected WebException(String msg, List<?> dataList) {
        super(msg);
        this.dataList = dataList;
    }

    protected WebException(String msg, List<?> dataList, Throwable cause) {
        super(msg, cause);
        this.dataList = dataList;
    }

    protected WebException(StatusCode statusCode, String msg, List<?> dataList) {
        super(statusCode, msg);
        this.dataList = dataList;
    }

    protected WebException() {
        super();
    }

    protected WebException(StatusCode statusCode) {
        super(statusCode);
    }

    protected WebException(String code, String msg) {
        super(code, msg);
    }

    public List<?> getDataList() { return this.dataList; }

}

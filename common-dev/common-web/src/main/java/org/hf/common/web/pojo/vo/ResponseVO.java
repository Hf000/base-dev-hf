package org.hf.common.web.pojo.vo;

import cn.hutool.core.date.DateUtil;
import org.hf.common.publi.interfac.ResponseResult;
import org.hf.common.publi.interfac.StatusCode;
import org.hf.common.web.enums.ResponseEnum;
import org.hf.common.web.utils.RequestParamUtil;

import java.io.Serializable;

/**
 * <p> 返回结果类 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2021/7/25 19:37
 */
public class ResponseVO<T> implements ResponseResult<T>, Serializable {

    private static final long serialVersionUID = -2073005438844022460L;

    protected String responseCode;
    protected String responseMsg;
    protected String requestId;
    protected Long timestamp;
    protected T data;

    public ResponseVO() {
        this.setResponseCode(ResponseEnum.SUCCESS.getCode());
        this.setResponseMsg(ResponseEnum.SUCCESS.getMsg());
        this.setTimestamp(DateUtil.current());
        this.setRequestId(RequestParamUtil.get());
    }

    public ResponseVO(String responseCode, String responseMsg) {
        this.setResponseCode(responseCode);
        this.setResponseMsg(responseMsg);
        this.setTimestamp(DateUtil.current());
        this.setRequestId(RequestParamUtil.get());
    }

    public ResponseVO(StatusCode statusCode) {
        this.setResponseCode(statusCode.getCode());
        this.setResponseMsg(statusCode.getMsg());
        this.setTimestamp(DateUtil.current());
        this.setRequestId(RequestParamUtil.get());
    }

    @Override
    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    @Override
    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Override
    public String toString() {
        return "ResponseVO{" +
                "responseCode='" + responseCode + '\'' +
                ", responseMsg='" + responseMsg + '\'' +
                ", requestId='" + requestId + '\'' +
                ", timestamp=" + timestamp +
                ", data=" + data +
                '}';
    }
}

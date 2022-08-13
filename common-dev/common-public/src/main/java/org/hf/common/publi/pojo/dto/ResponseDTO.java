package org.hf.common.publi.pojo.dto;

import org.hf.common.publi.enums.ResponseEnum;
import org.hf.common.publi.interfac.ResponseResult;
import org.hf.common.publi.interfac.StatusCode;

import java.io.Serializable;

/**
 * <p> 返回结果类 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2021/7/25 19:37
 */
public class ResponseDTO<T> implements ResponseResult<T>, Serializable {

    private static final long serialVersionUID = -2073005438844022460L;

    protected String responseCode;
    protected String responseMsg;
    protected T data;

    public ResponseDTO() {
        this.setResponseCode(ResponseEnum.SUCCESS.getCode());
        this.setResponseMsg(ResponseEnum.SUCCESS.getMsg());
    }

    public ResponseDTO(String responseCode, String responseMsg) {
        this.setResponseCode(responseCode);
        this.setResponseMsg(responseMsg);
    }

    public ResponseDTO(StatusCode statusCode) {
        this.setResponseCode(statusCode.getCode());
        this.setResponseMsg(statusCode.getMsg());
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

    @Override
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseVO{" +
                "responseCode='" + responseCode + '\'' +
                ", responseMsg='" + responseMsg + '\'' +
                ", data=" + data +
                '}';
    }
}

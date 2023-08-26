package org.hf.application.custom.rpc.core.base;

/**
 * 自定义请求基类
 */
public abstract class BaseRpcBean implements java.io.Serializable{

    private static final long serialVersionUID = 4332229479159998963L;
    /**
     *  请求id
     */
    private String requestId;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}

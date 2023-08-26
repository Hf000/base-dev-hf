package org.hf.application.custom.rpc.core.base;

/**
 * 响应参数
 */
public class RpcResponse extends BaseRpcBean{

    private static final long serialVersionUID = -7276479460065645174L;

    /**
     * 错误消息
     */
    private String errorMsg;
    /**
     * 结果数据
     */
    private Object result;

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "RpcResponse{" +
                "errorMsg='" + errorMsg + '\'' +
                ", result=" + result +
                '}';
    }
}
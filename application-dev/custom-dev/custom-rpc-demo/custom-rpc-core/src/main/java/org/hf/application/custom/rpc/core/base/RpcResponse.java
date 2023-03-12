package org.hf.application.custom.rpc.core.base;

public class RpcResponse extends BaseRpcBean{

    private static final long serialVersionUID = -7276479460065645174L;

    private String errorMsg; //错误消息
    private Object result; //结果数据

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
package org.hf.application.custom.rpc.core.base;

import java.util.Arrays;

/**
 * 请求入参
 */
public class RpcRequest extends BaseRpcBean {

    private static final long serialVersionUID = -4403913516658154989L;
    /**
     *  创建请求时间
     */
    private long createMillisTime;
    /**
     *  类名称，全包路径
     */
    private String className;
    /**
     * 执行方法名
     */
    private String methodName;
    /**
     * 方法中的参数类型
     */
    private Class<?>[] parameterTypes;
    /**
     * 执行方法传入的参数
     */
    private Object[] parameters;

    public long getCreateMillisTime() {
        return createMillisTime;
    }

    public void setCreateMillisTime(long createMillisTime) {
        this.createMillisTime = createMillisTime;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return "RpcRequest{" +
                "createMillisTime=" + createMillisTime +
                ", className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", parameterTypes=" + Arrays.toString(parameterTypes) +
                ", parameters=" + Arrays.toString(parameters) +
                '}';
    }
}
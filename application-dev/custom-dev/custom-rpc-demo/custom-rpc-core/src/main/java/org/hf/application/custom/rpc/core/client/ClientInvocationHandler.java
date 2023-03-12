package org.hf.application.custom.rpc.core.client;

import org.hf.application.custom.rpc.core.base.RpcRequest;
import org.hf.application.custom.rpc.core.base.RpcResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 动态代理的业务逻辑实现
 */
public class ClientInvocationHandler implements InvocationHandler {

    private NettyClient nettyClient;

    /**
     * 需要将NettyClient传入，因为要基于此发送消息数据
     *
     * @param nettyClient
     */
    public ClientInvocationHandler(NettyClient nettyClient) {
        this.nettyClient = nettyClient;
    }

    /**
     * 执行的业务逻辑
     *
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //获取类、方法、参数等信息
        String className = method.getDeclaringClass().getName();
        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] parameters = args;

        //封装请求对象
        RpcRequest request = new RpcRequest();
        request.setRequestId(UUID.randomUUID().toString());
        request.setCreateMillisTime(System.currentTimeMillis());
        request.setClassName(className);
        request.setMethodName(methodName);
        request.setParameterTypes(parameterTypes);
        request.setParameters(parameters);

        //定义异步的响应对象
        RpcFutureResponse futureResponse = new RpcFutureResponse(request);

        //向服务端发送消息
        nettyClient.sendMsg(request);

        //获取异步响应的消息
        RpcResponse rpcResponse = futureResponse.get(5, TimeUnit.SECONDS);
        if (rpcResponse.getErrorMsg() != null) {
            throw new RuntimeException(rpcResponse.getErrorMsg());
        }
        return rpcResponse.getResult();
    }

}

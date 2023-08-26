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

    private final NettyClient nettyClient;

    /**
     * 需要将NettyClient传入，因为要基于此发送消息数据
     */
    public ClientInvocationHandler(NettyClient nettyClient) {
        this.nettyClient = nettyClient;
    }

    /**
     * 执行的业务逻辑
     * @param proxy     指代我们所代理的那个真实对象
     * @param method    指代的是我们所要调用的真实对象的某个方法的Method对象
     * @param args      指代的是调用真实对象某个方法时的接收的参数
     * @return      结果
     * @throws Throwable 异常
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //获取类、方法、参数等信息
        String className = method.getDeclaringClass().getName();
        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        //封装请求对象
        RpcRequest request = new RpcRequest();
        request.setRequestId(UUID.randomUUID().toString());
        request.setCreateMillisTime(System.currentTimeMillis());
        request.setClassName(className);
        request.setMethodName(methodName);
        request.setParameterTypes(parameterTypes);
        request.setParameters(args);
        //定义异步的响应对象
        RpcFutureResponse futureResponse = new RpcFutureResponse(request);
        //向服务端发送消息，真正的订单提交操作就是这里发送到服务端然后服务端进行订单提交动作
        nettyClient.sendMsg(request);
        //获取异步响应的消息
        RpcResponse rpcResponse = futureResponse.get(5, TimeUnit.SECONDS);
        if (rpcResponse.getErrorMsg() != null) {
            throw new RuntimeException(rpcResponse.getErrorMsg());
        }
        return rpcResponse.getResult();
    }
}
package org.hf.application.custom.rpc.core.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.hf.application.custom.rpc.core.base.RpcRequest;
import org.hf.application.custom.rpc.core.base.RpcResponse;
import org.hf.application.custom.rpc.core.util.ClassUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerHandler.class);

    private static final Map<Class<?>, Object> OBJECT_MAP = new HashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest request) throws Exception {
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setRequestId(request.getRequestId());

        LOGGER.info("开始处理消息：requestId = " + request.getRequestId());

        try {
            Class<?> clazz = Class.forName(request.getClassName());
            if(!OBJECT_MAP.containsKey(clazz)){
                //获取接口的实现类，这里只获取第一个实现类，忽略其他实现类
                ArrayList<Class> allClassByInterface = ClassUtil.getAllClassByInterface(clazz);
                for (Class c : allClassByInterface) {
                    //将对象缓存起来，提升效率
                    OBJECT_MAP.put(clazz, c.newInstance());
                    break;
                }
            }

            //通过反射找到方法执行
            Method method = clazz.getMethod(request.getMethodName(), request.getParameterTypes());
            method.setAccessible(true);
            Object result = method.invoke(OBJECT_MAP.get(clazz), request.getParameters());

            rpcResponse.setResult(result);
        } catch (Exception e) {
            LOGGER.error("处理失败... requestId = " + request.getRequestId(), e);
            //出错
            rpcResponse.setErrorMsg("error");
        }

        ctx.writeAndFlush(rpcResponse);
    }

}

package org.hf.application.custom.rpc.core.server;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.hf.application.custom.rpc.core.base.RpcRequest;
import org.hf.application.custom.rpc.core.base.RpcResponse;
import org.hf.application.custom.rpc.core.util.ClassUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * // @ChannelHandler.Sharable注解作用: 如果channelhandler是⽆状态的（即不需要保存任何状态参数），那么使⽤Sharable注解，并在
 * bootstrap时只创建⼀个实例，减少GC。否则每次连接都会new出handler对象。
 * 同时需要注意ByteToMessageDecoder之类的编解码器是有状态的，不能使⽤Sharable注解。
 * 这里添加@ChannelHandler.Sharable注解后, 初始化的时候需要将此对象声明成成员变量,不能多次new,否则失效, 参考{@link ServerInitializer}
 */
@ChannelHandler.Sharable
public class ServerHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerHandler.class);

    private static final Map<Class<?>, Object> OBJECT_MAP = new HashMap<>();

    private int count;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object obj) {
        if (!(obj instanceof RpcRequest)) {
            throw new RuntimeException("消息类型有误");
        }
        RpcRequest request = (RpcRequest) obj;
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setRequestId(request.getRequestId());
        LOGGER.info("开始处理消息：requestId = " + request.getRequestId());
        try {
            Class<?> clazz = Class.forName(request.getClassName());
            if(!OBJECT_MAP.containsKey(clazz)){
                //获取接口的实现类，这里只获取第一个实现类，忽略其他实现类
                ArrayList<Class<?>> allClassByInterface = ClassUtil.getAllClassByInterface(clazz);
                for (Class<?> c : allClassByInterface) {
                    //将对象缓存起来，提升效率
                    OBJECT_MAP.put(clazz, c.newInstance());
                    break;
                }
            }
            //通过反射找到方法执行
            Method method = clazz.getMethod(request.getMethodName(), request.getParameterTypes());
            method.setAccessible(true);
            // 进行方法调用，并将执行结果赋值到响应对象中
            Object result = method.invoke(OBJECT_MAP.get(clazz), request.getParameters());
            rpcResponse.setResult(result);
        } catch (Exception e) {
            //出错
            LOGGER.error("处理失败... requestId = " + request.getRequestId(), e);
            rpcResponse.setErrorMsg("error");
        }
        // 响应消息给客户端
        ctx.writeAndFlush(rpcResponse);
        // 这里是为了测试@ChannelHandler.Sharable是否起作用
        System.out.println("测试一下" + ++count);
    }
}
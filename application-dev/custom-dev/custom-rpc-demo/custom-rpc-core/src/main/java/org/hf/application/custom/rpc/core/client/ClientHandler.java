package org.hf.application.custom.rpc.core.client;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.hf.application.custom.rpc.core.base.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * // @ChannelHandler.Sharable注解作用: 如果channelhandler是⽆状态的（即不需要保存任何状态参数），那么使⽤Sharable注解，并在
 * bootstrap时只创建⼀个实例，减少GC。否则每次连接都会new出handler对象。
 * 同时需要注意ByteToMessageDecoder之类的编解码器是有状态的，不能使⽤Sharable注解。
 * 这里添加@ChannelHandler.Sharable注解后, 初始化的时候需要将此对象声明成成员变量,不能多次new,否则失效, 参考{@link ClientInitializer}
 */
@ChannelHandler.Sharable
public class ClientHandler extends SimpleChannelInboundHandler<RpcResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse msg) throws Exception {
        LOGGER.info("接收到服务端的消息：requestId = " + msg.getRequestId());
        //接收到服务端的消息后，通知响应已经完成 通过RpcFutureResponse中的Map获取到RpcFutureResponse对象，进行方法回调
        RpcFutureResponse rpcFutureResponse = RpcFutureResponse.getRpcFutureResponse(msg.getRequestId());
        if(null != rpcFutureResponse){
            // 将服务端发送过来的响应内容赋值到对应的请求响应对象中
            rpcFutureResponse.setResponse(msg);
        }else{
            LOGGER.warn("没有找到对应的RpcFutureResponse对象~ requestId = " + msg.getRequestId());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("客户端出错~ ", cause);
        ctx.close();
    }
}
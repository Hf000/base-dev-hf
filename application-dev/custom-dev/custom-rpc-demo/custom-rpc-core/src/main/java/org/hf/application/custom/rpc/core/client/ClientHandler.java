package org.hf.application.custom.rpc.core.client;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.hf.application.custom.rpc.core.base.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ChannelHandler.Sharable
public class ClientHandler extends SimpleChannelInboundHandler<RpcResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse msg) throws Exception {

        LOGGER.info("接收到服务端的消息：requestId = " + msg.getRequestId());

        //接收到服务端的消息后，通知响应已经完成
        //通过RpcFutureResponse中的Map获取到RpcFutureResponse对象，进行方法回调
        RpcFutureResponse rpcFutureResponse = RpcFutureResponse.getRpcFutureResponse(msg.getRequestId());
        if(null != rpcFutureResponse){
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
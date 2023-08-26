package org.hf.application.netty.demo.simple.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //这里可以直接拿到Integer类型的数据
        Integer i = (Integer) msg;
        System.out.println("服务端接收到的消息为：" + i);
    }

}

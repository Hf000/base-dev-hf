package org.hf.application.netty.demo.obj.codec;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class ServerHandler extends SimpleChannelInboundHandler<User> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, User user) throws Exception {
        // 获取到user对象
        System.out.println(user);
        // 向客户端发送消息
        ctx.writeAndFlush(Unpooled.copiedBuffer("ok", CharsetUtil.UTF_8));
    }
}
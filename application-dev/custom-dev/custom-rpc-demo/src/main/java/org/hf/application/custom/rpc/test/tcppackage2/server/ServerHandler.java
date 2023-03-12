package org.hf.application.custom.rpc.test.tcppackage2.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import org.hf.application.custom.rpc.test.tcppackage2.MyProtocol;

public class ServerHandler extends SimpleChannelInboundHandler<MyProtocol> {

    private int count;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyProtocol msg) throws Exception {
        System.out.println("服务端接收到消息：" + new String(msg.getBody(), CharsetUtil.UTF_8));
        System.out.println("服务端接收到消息数量：" + (++count));

        byte[] data = "ok".getBytes(CharsetUtil.UTF_8);
        MyProtocol myProtocol = new MyProtocol();
        myProtocol.setLength(data.length);
        myProtocol.setBody(data);

        ctx.writeAndFlush(myProtocol);
    }
}
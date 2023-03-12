package org.hf.application.custom.rpc.test.tcppackage2.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import org.hf.application.custom.rpc.test.tcppackage2.MyProtocol;

public class ClientHandler extends SimpleChannelInboundHandler<MyProtocol> {

    private int count;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyProtocol msg) throws Exception {
        System.out.println("接收到服务端的消息：" + new String(msg.getBody(), CharsetUtil.UTF_8));
        System.out.println("接收到服务端的消息数量：" + (++count));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 10; i++) {
            byte[] data = "from client a message!".getBytes(CharsetUtil.UTF_8);

            MyProtocol myProtocol = new MyProtocol();
            myProtocol.setLength(data.length);
            myProtocol.setBody(data);

            ctx.writeAndFlush(myProtocol);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
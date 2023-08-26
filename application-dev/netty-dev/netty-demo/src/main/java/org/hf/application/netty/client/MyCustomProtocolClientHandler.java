package org.hf.application.netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import org.hf.application.netty.codec.MyProtocol;

/**
 * <p> 客户端处理器 </p >
 * @author hufei
 * @date 2022/8/6 20:41
*/
public class MyCustomProtocolClientHandler extends SimpleChannelInboundHandler<MyProtocol> {

    private int count;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MyProtocol byteBuf) throws Exception {
        System.out.println("接收到服务端的消息:" + new String(byteBuf.getBody(), CharsetUtil.UTF_8));
        System.out.println("接收到服务端的消息数量：" + (++count));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 多次给服务端发送消息, 这里自定义协议和编解码后解决了多次发送消息的问题
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
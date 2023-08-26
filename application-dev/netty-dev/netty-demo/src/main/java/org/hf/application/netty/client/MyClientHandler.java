package org.hf.application.netty.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * <p> 客户端处理器 </p >
 * @author hufei
 * @date 2022/8/6 20:41
 */
public class MyClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private int count;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        System.out.println("接收到服务端的消息:" + byteBuf.toString(CharsetUtil.UTF_8));
        System.out.println("接收到服务端的消息数量：" + (++count));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        String msg = "hello";
//        ctx.writeAndFlush(Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8));
        // 多次给服务端发送消息 这里没有解决粘包/拆包问题, 会一次发送
        for (int i = 0; i < 10; i++) {
            if (i == 9) {
                TimeUnit.SECONDS.sleep(5);
            }
            ctx.writeAndFlush(Unpooled.copiedBuffer("from client a message!", CharsetUtil.UTF_8));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
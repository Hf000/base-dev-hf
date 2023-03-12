package org.hf.application.custom.rpc.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class MyChannelHandler extends ChannelInboundHandlerAdapter {

    /**
     * 获取客户端发来的数据
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        String msgStr = byteBuf.toString(CharsetUtil.UTF_8);
        System.out.println("客户端发来数据：" + msgStr);

        //向客户端发送数据
        ctx.writeAndFlush(Unpooled.copiedBuffer("ok", CharsetUtil.UTF_8));

        //手动释放资源
//        ReferenceCountUtil.release(byteBuf);

        ctx.fireChannelRead(msg); //将ByteBuf向下传递
    }

    /**
     * 异常处理
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
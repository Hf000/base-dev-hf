package org.hf.application.netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * 数据处理handler
 * ChannelInboundHandlerAdapter 与 SimpleChannelInboundHandler的区别：
 *  1> 在服务端编写ChannelHandler时继承的是ChannelInboundHandlerAdapter
 *  2> 在客户端编写ChannelHandler时继承的是SimpleChannelInboundHandler
 *  3> 两者的区别在于，前者不会释放消息数据的引⽤，⽽后者会释放消息数据的引⽤。服务端需要在收到消息并处理完成后才能进行释放,
 *  因为write操作异步, 直到 channelRead () 方法返回后可能仍然没有完成 为此扩展了 ChannelInboundHandlerAdapter ，
 *  其在这个时间点上不会释放消息。 在客户端，当 channelRead0 () 方法完成时，你已经有了传入消息，并且已经处理完它了。就可以释放了
 * @author hf
 */
public class MyCustomChannelHandler extends ChannelInboundHandlerAdapter {

    /**
     * 获取客户端发来的数据
     * @param ctx           数据处理链上下文, 记录数据从入站到出站的流转顺序
     * @param msg           数据内容
     * @throws Exception    异常
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
        // 将ByteBuf向下传递, 由流⽔线最后⼀棒 TailHandler 完成⾃动释放。
        ctx.fireChannelRead(msg);
    }

    /**
     * 异常处理
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
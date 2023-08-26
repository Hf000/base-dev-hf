package org.hf.application.netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * <p> 服务端消息处理 不推荐, 原因参考{@link org.hf.application.netty.server.MyCustomChannelHandler} </p >
 * @author hufei
 * @date 2022/8/6 20:42
 */
public class MyChannelHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private int count;

    /**
     * 此方法必须重写
     * @param ctx               数据处理链上下文, 记录数据从入站到出站的流转顺序
     * @param byteBuf           数据内容
     * @throws Exception        异常
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf byteBuf) throws Exception {
        System.out.println("服务端channelRead0()方法执行了吗???");
        System.out.println("服务端接收到消息：" + byteBuf.toString(CharsetUtil.UTF_8));
        System.out.println("服务端接收到消息数量：" + (++count));
        ctx.writeAndFlush(Unpooled.copiedBuffer("ok", CharsetUtil.UTF_8));
    }

    /**
     * 获取客户端发来的数据 此方法可以不重写, 可以直接重写channelRead0方法
     * 这个方法的父类方法逻辑 channelRead中调用了channelRead0，其会先做消息类型检查，判断当前message 是否需要传递到下一个handler。
     */
    /*@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        String msgStr = byteBuf.toString(CharsetUtil.UTF_8);
        System.out.println("客户端发来数据：" + msgStr);
        //向客户端发送消息
        ctx.writeAndFlush(Unpooled.copiedBuffer("ok", CharsetUtil.UTF_8));
    }*/

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}

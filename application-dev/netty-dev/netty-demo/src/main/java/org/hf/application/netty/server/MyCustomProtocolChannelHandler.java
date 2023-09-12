package org.hf.application.netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import org.hf.application.netty.codec.MyProtocol;

/**
 * 数据处理handler
 * ChannelInboundHandlerAdapter 与 SimpleChannelInboundHandler的区别：
 *  1> 在服务端编写ChannelHandler时继承的是ChannelInboundHandlerAdapter
 *  2> 在客户端编写ChannelHandler时继承的是SimpleChannelInboundHandler
 *  3> 两者的区别在于，前者不会释放消息数据的引⽤，⽽后者会释放消息数据的引⽤。服务端需要在收到消息并处理完成后才能进行释放,
 *  因为write操作异步, 直到 channelRead () 方法返回后可能仍然没有完成 为此扩展了 ChannelInboundHandlerAdapter ，
 *  其在这个时间点上不会释放消息。 在客户端，当 channelRead0 () 方法完成时，你已经有了传入消息，并且已经处理完它了。就可以释放了
 * @author hf
 * // @ChannelHandler.Sharable注解作用: 如果channelhandler是⽆状态的（即不需要保存任何状态参数），那么使⽤Sharable注解，并在
 * bootstrap时只创建⼀个实例，减少GC。否则每次连接都会new出handler对象。
 * 同时需要注意ByteToMessageDecoder之类的编解码器是有状态的，不能使⽤Sharable注解。
 * 这里添加@ChannelHandler.Sharable注解后, 初始化的时候需要将此对象声明成成员变量,不能多次new,否则失效, 参考{@link MyChannelInitializer}
 */
@ChannelHandler.Sharable
public class MyCustomProtocolChannelHandler extends ChannelInboundHandlerAdapter {

    private int count;

    /**
     * 获取客户端发来的数据
     * @param ctx           数据处理链上下文, 记录数据从入站到出站的流转顺序
     * @param msgObj        数据内容
     * @throws Exception    异常
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msgObj) throws Exception {
        if (msgObj instanceof MyProtocol) {
            MyProtocol msg = (MyProtocol) msgObj;
            System.out.println("服务端接收到消息：" + new String(msg.getBody(), CharsetUtil.UTF_8));
            System.out.println(++count);
            byte[] data = "ok".getBytes(CharsetUtil.UTF_8);
            MyProtocol myProtocol = new MyProtocol();
            myProtocol.setLength(data.length);
            myProtocol.setBody(data);
            // 将msg从整个ChannelPipline中⾛⼀遍，所有的handler都要经过
//            ctx.channel().writeAndFlush(myProtocol);
            // 从当前handler⼀直到pipline的尾部，调⽤更短
            ctx.writeAndFlush(myProtocol);
            // 将ByteBuf向下传递, 由流⽔线最后⼀棒 TailHandler 完成⾃动释放。 如果采用的是直接缓冲区那么就必须进行释放
            ctx.fireChannelRead(msg);
        } else {
            ByteBuf byteBuf = (ByteBuf) msgObj;
            String msgStr = byteBuf.toString(CharsetUtil.UTF_8);
            System.out.println("客户端发来数据：" + msgStr);
            //向客户端发送数据
            ctx.writeAndFlush(Unpooled.copiedBuffer("ok", CharsetUtil.UTF_8));
            //手动释放资源  如果采用的是直接缓冲区那么就必须进行释放， 注意要手动释放就都手动释放,一定要保持一致
//        ReferenceCountUtil.release(byteBuf);
            // 将ByteBuf向下传递, 由流⽔线最后⼀棒 TailHandler 完成⾃动释放。 如果采用的是直接缓冲区那么就必须进行释放
            ctx.fireChannelRead(msgObj);
        }
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
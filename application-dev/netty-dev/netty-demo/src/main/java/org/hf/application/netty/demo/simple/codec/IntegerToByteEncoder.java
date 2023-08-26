package org.hf.application.netty.demo.simple.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 编码器的简单应用
 * 编码器与解码器是相反的操作，将原有的格式转化为字节的过程，在Netty中提供了
 * MessageToByteEncoder的抽象实现，它实现了ChannelOutboundHandler接⼝，本质上也是
 * ChannelHandler。⼀些实现类：
 * 1. ObjectEncoder 将对象（需要实现Serializable接⼝）编码为字节流
 * 2. SocksMessageEncoder 将SocksMessage编码为字节流
 * 3. HAProxyMessageEncoder 将HAProxyMessage编码成字节流
 * Netty也提供了MessageToMessageEncoder，将⼀种格式转化为另⼀种格式的编码器，也提供了⼀些实现：
 * 1. RedisEncoder 将Redis协议的对象进⾏编码
 * 2. StringEncoder 将字符串进⾏编码操作
 * 3. Base64Encoder 将Base64字符串进⾏编码操作
 */
public class IntegerToByteEncoder extends MessageToByteEncoder<Integer> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Integer msg, ByteBuf out) throws Exception {
        out.writeInt(msg);
    }
}

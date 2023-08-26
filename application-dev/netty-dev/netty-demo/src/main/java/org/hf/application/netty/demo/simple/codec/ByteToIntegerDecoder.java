package org.hf.application.netty.demo.simple.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 解码器的简单应用
 * Netty中提供了ByteToMessageDecoder的抽象实现，⾃定义解码器只需要继承该类，实现decode()即
 * 可。Netty也提供了⼀些常⽤的解码器实现，基本都是开箱即⽤的
 * 1. RedisDecoder 基于Redis协议的解码器
 * 2. XmlDecoder 基于XML格式的解码器
 * 3. JsonObjectDecoder 基于json数据格式的解码器
 * 4. HttpObjectDecoder 基于http协议的解码器
 * Netty也提供了MessageToMessageDecoder，将⼀种格式转化为另⼀种格式的解码器，也提供了⼀些
 * 实现：
 * 1. StringDecoder 将接收到ByteBuf转化为字符串
 * 2. ByteArrayDecoder 将接收到ByteBuf转化字节数组
 * 3. Base64Decoder 将由ByteBuf或US-ASCII字符串编码的Base64解码为ByteBuf。
 */
public class ByteToIntegerDecoder extends ByteToMessageDecoder {

    /**
     *
     * @param ctx 上下文
     * @param in 输入的ByteBuf消息数据
     * @param out 转化后输出的容器
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //int类型占用4个字节，所以需要判断是否存在有4个字节，再进行读取
        if(in.readableBytes() >= 4){
            //读取到int类型数据，放入到输出，完成数据类型的转化
            out.add(in.readInt());
        }
    }
}
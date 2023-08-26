package org.hf.application.netty.demo.simple.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * 1.使⽤了特殊的ByteBuf，叫做ReplayingDecoderByteBuf，扩展了ByteBuf
 * 2.重写了ByteBuf的readXxx()等⽅法，会先检查可读字节⻓度，⼀旦检测到不满⾜要求就直接抛出
 * REPLAY（REPLAY继承ERROR）
 * 3.ReplayingDecoder重写了ByteToMessageDecoder的callDecode()⽅法，捕获Signal并在catch块
 * 中重置ByteBuf的readerIndex。
 * 4.继续等待数据，直到有了数据后继续读取，这样就可以保证读取到需要读取的数据。
 * 5.类定义中的泛型 S 是⼀个⽤于记录解码状态的状态机枚举类，在state(S s)、checkpoint(S s)等⽅
 * 法中会⽤到。在简单解码时也可以⽤java.lang.Void来占位。
 */
public class ByteToIntegerReplayingDecoder extends ReplayingDecoder<Void> {

    /**
     * @param ctx 上下文
     * @param in  输入的ByteBuf消息数据
     * @param out 转化后输出的容器
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //读取到int类型数据，放入到输出，完成数据类型的转化
        out.add(in.readInt());
    }

}
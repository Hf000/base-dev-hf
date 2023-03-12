package org.hf.application.custom.rpc.test.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

public class ByteToIntegerDecoder2 extends ReplayingDecoder<Void> {

    /**
     * @param ctx 上下文
     * @param in  输入的ByteBuf消息数据
     * @param out 转化后输出的容器
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        out.add(in.readInt()); //读取到int类型数据，放入到输出，完成数据类型的转化
    }

}
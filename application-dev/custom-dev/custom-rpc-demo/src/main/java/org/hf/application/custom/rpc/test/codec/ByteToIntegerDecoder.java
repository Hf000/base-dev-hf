package org.hf.application.custom.rpc.test.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class ByteToIntegerDecoder extends ByteToMessageDecoder {

    /**
     *
     * @param ctx 上下文
     * @param in 输入的ByteBuf消息数据
     * @param out 转化后输出的容器
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if(in.readableBytes() >= 4){ //int类型占用4个字节，所以需要判断是否存在有4个字节，再进行读取
            out.add(in.readInt()); //读取到int类型数据，放入到输出，完成数据类型的转化
        }
    }

}

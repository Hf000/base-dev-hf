package org.hf.application.custom.rpc.test.codec.hessian;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class HessianDecoder extends ByteToMessageDecoder {

    private HessianSerializer hessianSerializer = new HessianSerializer();

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //复制一份ByteBuf数据，轻复制，非完全拷贝
        //避免出现异常：did not read anything but decoded a message
        //Netty检测没有读取任何字节就会抛出该异常
        ByteBuf in2 = in.retainedDuplicate();

        byte[] dst;
        if (in2.hasArray()) {//堆缓冲区模式
            dst = in2.array();
        } else {
            dst = new byte[in2.readableBytes()];
            in2.getBytes(in2.readerIndex(), dst);
        }

        in.skipBytes(in.readableBytes());//跳过所有的字节，表示已经读取过了

        Object obj = hessianSerializer.deserialize(dst, User.class);//反序列化
        out.add(obj);
    }
}

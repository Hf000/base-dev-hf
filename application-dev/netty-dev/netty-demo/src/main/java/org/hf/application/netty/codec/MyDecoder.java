package org.hf.application.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * 解码器
 * 用于解决TCP的粘包/拆包问题
 * @author hf
 */
public class MyDecoder extends ReplayingDecoder<Void> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //获取长度
        int length = in.readInt();
        //根据长度定义byte数组
        byte[] data = new byte[length];
        //读取数据
        in.readBytes(data);

        MyProtocol myProtocol = new MyProtocol();
        myProtocol.setLength(length);
        myProtocol.setBody(data);

        out.add(myProtocol);
    }

}
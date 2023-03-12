package org.hf.application.custom.rpc.test.tcppackage2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

public class MyDecoder extends ReplayingDecoder<Void> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        int length = in.readInt(); //获取长度
        byte[] data = new byte[length]; //根据长度定义byte数组
        in.readBytes(data); //读取数据

        MyProtocol myProtocol = new MyProtocol();
        myProtocol.setLength(length);
        myProtocol.setBody(data);

        out.add(myProtocol);
    }

}
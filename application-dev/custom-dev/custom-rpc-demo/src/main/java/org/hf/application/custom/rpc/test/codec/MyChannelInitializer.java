package org.hf.application.custom.rpc.test.codec;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * ChannelHandler的初始化
 */
public class MyChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //将业务处理器加入到列表中
        ch.pipeline().addLast(new ByteToIntegerDecoder2()) //解码器
                .addLast(new ServerHandler());
    }
}

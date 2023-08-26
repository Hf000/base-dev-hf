package org.hf.application.netty.demo.simple.codec;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * ChannelHandler的初始化
 */
public class MyChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //将业务处理器加入到列表中, 先添加解码器再添加服务端处理器
//        ch.pipeline().addLast(new ByteToIntegerDecoder()).addLast(new ServerHandler());
        // 这里的ByteToIntegerReplayingDecoder继承ReplayingDecoder, 扩展了对于不同类型的字节长度判断, 如果像上面直接继承ByteToMessageDecoder则需要自行判断
        ch.pipeline().addLast(new ByteToIntegerReplayingDecoder()).addLast(new ServerHandler());
    }
}

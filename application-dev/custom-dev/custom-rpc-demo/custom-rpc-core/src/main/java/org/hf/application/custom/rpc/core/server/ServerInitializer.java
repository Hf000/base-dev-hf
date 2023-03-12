package org.hf.application.custom.rpc.core.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.hf.application.custom.rpc.core.base.RpcRequest;
import org.hf.application.custom.rpc.core.codec.MyDecoder;
import org.hf.application.custom.rpc.core.codec.MyEncoder;

public class ServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline()
                .addLast(new MyDecoder(RpcRequest.class)) //解码器，需要解码的对象是RpcRequest
                .addLast(new MyEncoder()) //编码器，用于数据的响应
                .addLast(new ServerHandler()); //自定义逻辑
    }
}

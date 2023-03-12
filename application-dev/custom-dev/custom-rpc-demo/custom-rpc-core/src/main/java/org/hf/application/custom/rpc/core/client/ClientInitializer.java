package org.hf.application.custom.rpc.core.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.hf.application.custom.rpc.core.base.RpcResponse;
import org.hf.application.custom.rpc.core.codec.MyDecoder;
import org.hf.application.custom.rpc.core.codec.MyEncoder;

public class ClientInitializer extends ChannelInitializer<SocketChannel> {

    private static final ClientHandler CLIENT_HANDLER = new ClientHandler();

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline()
            .addLast(new MyDecoder(RpcResponse.class))
            .addLast(new MyEncoder())
            .addLast(CLIENT_HANDLER);
    }
}
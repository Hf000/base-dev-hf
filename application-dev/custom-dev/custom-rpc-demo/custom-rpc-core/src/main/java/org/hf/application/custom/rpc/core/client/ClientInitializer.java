package org.hf.application.custom.rpc.core.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.hf.application.custom.rpc.core.base.RpcResponse;
import org.hf.application.custom.rpc.core.codec.MyDecoder;
import org.hf.application.custom.rpc.core.codec.MyEncoder;

public class ClientInitializer extends ChannelInitializer<SocketChannel> {

    /**
     * 这里声明了处理器，那么clientHandler需要@ChannelHandler.Sharable注解修饰，否则会报错，因为不同连接不能多次添加同一个处理器对象
     */
    private static final ClientHandler CLIENT_HANDLER = new ClientHandler();

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline()
            .addLast(new MyDecoder<>(RpcResponse.class))
            .addLast(new MyEncoder())
            .addLast(CLIENT_HANDLER);
    }
}
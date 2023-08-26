package org.hf.application.custom.rpc.core.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.hf.application.custom.rpc.core.base.RpcRequest;
import org.hf.application.custom.rpc.core.codec.MyDecoder;
import org.hf.application.custom.rpc.core.codec.MyEncoder;

public class ServerInitializer extends ChannelInitializer<SocketChannel> {

    /**
     * 这里声明了处理器, 那么ServerHandler需要@ChannelHandler.Sharable注解修饰,否则会报错,不同链接不能多次添加同一个处理器对象
     */
    private static final ServerHandler SERVER_HANDLER = new ServerHandler();

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        // 添加处理器链
        ch.pipeline()
                //解码器，需要解码的对象是RpcRequest
                .addLast(new MyDecoder<>(RpcRequest.class))
                //编码器，用于数据的响应
                .addLast(new MyEncoder())
                //自定义逻辑
                .addLast(SERVER_HANDLER);
    }
}

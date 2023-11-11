package org.hf.application.netty.demo.obj.codec;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class NettyObjectClient {

    public static void main(String[] args) throws Exception{
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            // 服务器启动类
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(worker);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    // 设置编码器
                    ch.pipeline().addLast(new ObjectEncoder());
                    ch.pipeline().addLast(new ClientHandler());
                }
            });
            // 配置连接地址
            ChannelFuture future = bootstrap.connect("127.0.0.1", 6677).sync();
            // 连接成功后将持续阻塞该线程不会执行到finally中去
            future.channel().closeFuture().sync();
        } finally {
            worker.shutdownGracefully();
        }
    }
}

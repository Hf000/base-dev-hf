package org.hf.application.netty.demo.hessian.codec;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyHessianClient {

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
                    ch.pipeline().addLast(new HessianEncoder());
                    ch.pipeline().addLast(new ClientHandler());
                }
            });
            ChannelFuture future = bootstrap.connect("127.0.0.1", 6677).sync();
            future.channel().closeFuture().sync();
        } finally {
            worker.shutdownGracefully();
        }
    }
}
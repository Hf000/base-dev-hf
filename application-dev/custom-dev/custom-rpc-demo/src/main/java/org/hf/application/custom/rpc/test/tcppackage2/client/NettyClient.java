package org.hf.application.custom.rpc.test.tcppackage2.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.hf.application.custom.rpc.test.tcppackage2.MyDecoder;
import org.hf.application.custom.rpc.test.tcppackage2.MyEncoder;

public class NettyClient {

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
                    ch.pipeline().addLast(new MyEncoder());
                    ch.pipeline().addLast(new MyDecoder());
                    ch.pipeline().addLast(new ClientHandler());
                }
            });

            ChannelFuture future = bootstrap.connect("127.0.0.1", 5566).sync();

            future.channel().closeFuture().sync();
        } finally {
            worker.shutdownGracefully();
        }
    }
}
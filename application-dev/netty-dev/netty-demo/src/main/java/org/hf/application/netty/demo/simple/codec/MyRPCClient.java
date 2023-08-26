package org.hf.application.netty.demo.simple.codec;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class MyRPCClient {

    public void start(String host, int port) throws Exception {
        //定义工作线程组
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            //注意：client使用的是Bootstrap
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(worker)
                    //注意：client使用的是NioSocketChannel
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 先添加编码器再添加客户端处理器到执行链
                            ch.pipeline().addLast(new IntegerToByteEncoder()).addLast(new ClientHandler());
                        }
                    });
            //连接到远程服务
            ChannelFuture future = bootstrap.connect(host, port).sync();
            future.channel().closeFuture().sync();
        } finally {
            worker.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new MyRPCClient().start("127.0.0.1", 5566);
    }
}

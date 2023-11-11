package org.hf.application.netty.demo.obj.codec;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;

public class NettyObjectServer {

    public static void main(String[] args) throws Exception {
        // 主线程，不处理任何业务逻辑，只是接收客户的连接请求
        EventLoopGroup boss = new NioEventLoopGroup(1);
        // 工作线程，线程数默认是：cpu*2
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            // 服务器启动类
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boss, worker);
            //配置server通道
            serverBootstrap.channel(NioServerSocketChannel.class);
            //worker线程的处理器
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline()
                            // 添加对象解码器
                            .addLast(new ObjectDecoder(ClassResolvers.weakCachingResolver(this.getClass().getClassLoader())))
                            .addLast(new ServerHandler());
                }
            });
            // 绑定端口号
            ChannelFuture future = serverBootstrap.bind(6677).sync();
            System.out.println("服务器启动完成。。。。。");
            //等待服务端监听端口关闭 连接成功后将持续阻塞该线程不会执行到finally中去
            future.channel().closeFuture().sync();
        } finally {
            //优雅关闭
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
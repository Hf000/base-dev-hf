package org.hf.application.custom.rpc.core.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyServer.class);

    public static void start(String host, int port) {
        doStart(host, port);
    }

    private static void doStart(String host, int port) {
        // 主线程，不处理任何业务逻辑，只是接收客户的连接请求
        EventLoopGroup boss = new NioEventLoopGroup(1);
        // 工作线程，线程数默认是：cpu*2
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            // 服务器启动类
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //设置线程组
            serverBootstrap.group(boss, worker)
                    //配置server通道
                    .channel(NioServerSocketChannel.class)
                    //worker线程的处理器
                    .childHandler(new ServerInitializer());
            // 绑定端口号
            ChannelFuture future = serverBootstrap.bind(host, port).sync();
            LOGGER.info("服务器启动完成，地址为：" + host + ":" + port);
            //等待服务端监听端口关闭 连接成功后将持续阻塞该线程不会执行到finally中去
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            LOGGER.error("服务器启动失败！",e);
        } finally {
            //优雅关闭
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
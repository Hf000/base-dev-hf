package org.hf.application.netty.tcp.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.hf.application.netty.tcp.server.utils.SpringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

/**
 * @author hf
 * 自定义实现tcp消息服务端 - 1 netty服务端
 */
@Slf4j
@Component
public class NettyServer extends Thread {

    /**
     * netty绑定的端口号
     */
    @Value("${server.netty.port}")
    public Integer port;

    @Override
    public void run() {
        // 服务端初始化
        startServer();
    }

    private void startServer() {
        EventLoopGroup bossGroup = null;
        EventLoopGroup workGroup = null;
        ChannelFuture future;
        try {
            // 创建主线程, 不处理任何业务, 只负责客户端连接
            bossGroup = new NioEventLoopGroup();
            // 创建工作线程,  默认是CPU核数*2
            workGroup = new NioEventLoopGroup();
            // 服务器启动类
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            // 绑定线程组
            serverBootstrap.group(bossGroup, workGroup)
                    // 配置服务端通道
                    .channel(NioServerSocketChannel.class)
                    // 配置处理器
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(SpringUtils.getBean(NettyServerHandler.class));
                        }
                    })
                    // 配置参数
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            // 绑定端口
            future = serverBootstrap.bind(new InetSocketAddress(port)).sync();
            if (future != null) {
                try {
                    // 连接成功后将持续阻塞该线程
                    future.channel().closeFuture().sync();
                } catch (InterruptedException e) {
                    log.error("channel关闭异常：", e);
                }
            }
            log.info(" *************TCP服务端启动成功 Port：{}*********** ", port);
        } catch (Exception e) {
            log.error("TCP服务端启动异常", e);
        } finally {
            if (bossGroup != null) {
                //线程组资源回收
                bossGroup.shutdownGracefully();
            }
            if (workGroup != null) {
                //线程组资源回收
                workGroup.shutdownGracefully();
            }
        }
    }
}
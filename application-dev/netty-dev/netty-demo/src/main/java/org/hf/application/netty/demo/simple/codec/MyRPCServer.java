package org.hf.application.netty.demo.simple.codec;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class MyRPCServer {

    public void start(int port) throws Exception {
        // 主线程，不处理任何业务逻辑，只是接收客户的连接请求
        EventLoopGroup boss = new NioEventLoopGroup(1);
        // 工作线程，线程数默认是：cpu核数*2
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            // 服务器启动类
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //设置线程组
            serverBootstrap.group(boss, worker)
                    //配置server通道
                    .channel(NioServerSocketChannel.class)
                    //worker线程的处理器
                    .childHandler(new MyChannelInitializer());
            //ByteBuf 的分配要设置为非池化，否则不能切换到堆缓冲区模式
            serverBootstrap.childOption(ChannelOption.ALLOCATOR, UnpooledByteBufAllocator.DEFAULT);
            ChannelFuture future = serverBootstrap.bind(port).sync();
            System.out.println("服务器启动完成，端口为：" + port);
            //等待服务端监听端口关闭 连接成功后将持续阻塞该线程不会执行到finally中去
            future.channel().closeFuture().sync();
        } finally {
            //优雅关闭
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new MyRPCServer().start(5566);
    }

}
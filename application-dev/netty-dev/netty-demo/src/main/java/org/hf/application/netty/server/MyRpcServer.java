package org.hf.application.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * <p> rpc服务 初始化消息处理器 </p >
 * @author hufei
 * @date 2022/8/6 20:42
 */
public class MyRpcServer {

    public void start(int port) {
        EventLoopGroup boss = null;
        EventLoopGroup worker = null;
        try {
            //创建主线程, 不处理任何业务, 只负责客户端连接
            boss = new NioEventLoopGroup(1);
            //创建工作线程,  默认是CPU核数*2
            worker = new NioEventLoopGroup();
            //服务器启动类
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //设置工作线程组
            serverBootstrap.group(boss, worker)
                    //配置server通道
                    .channel(NioServerSocketChannel.class)
                    // 设置worker线程的处理器, 如果这里只有单个处理器也可以直接加载
//                    .childHandler(new MyCustomChannelHandler())
                    // 设置worker线程的处理器, 这里可以设置多个处理器也可以设置单个处理器
                    .childHandler(new MyChannelInitializer());
            // 默认是池化分配， ByteBuf 的分配要设置为非池化，否则不能切换到堆缓冲区模式, 搭配System.setProperty("io.netty.noUnsafe", "true");使用
//            serverBootstrap.childOption(ChannelOption.ALLOCATOR, UnpooledByteBufAllocator.DEFAULT);
            //绑定端口
            ChannelFuture future = serverBootstrap.bind(port).sync();
            System.out.println("服务端启动完成。。。。。");
            //等待服务端监听端口关闭
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (boss != null) {
                //关闭主线程
                boss.shutdownGracefully();
            }
            if (worker != null) {
                //关闭工作线程
                worker.shutdownGracefully();
            }
        }
    }

}

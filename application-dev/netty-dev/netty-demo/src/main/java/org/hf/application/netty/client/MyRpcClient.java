package org.hf.application.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.hf.application.netty.codec.MyDecoder;
import org.hf.application.netty.codec.MyEncoder;

/**
 * <p> rpc客户端 </p >
 * @author hufei
 * @date 2022/8/6 20:41
 */
public class MyRpcClient {

    public void start(String host, int port) {
        EventLoopGroup worker = null;
        try {
            //创建工作线程组
            worker = new NioEventLoopGroup();
            //开启client客户端
            Bootstrap bootstrap = new Bootstrap();
            //设置工作线程组
            bootstrap.group(worker)
                    //设置socket通道
                    .channel(NioSocketChannel.class)
            //设置业务处理handler, 单个处理器可以直接加载, 此handler会有粘包/拆包问题
//                    .handler(new MyClientHandler());
            // 设置通过自定义初始化的方式加载handler, 这里可以设置多个数据处理器链路 此handler解决粘包/拆包问题
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new MyEncoder());
                            ch.pipeline().addLast(new MyDecoder());
                            ch.pipeline().addLast(new MyCustomProtocolClientHandler());
                        }
                    });
            //建立远程连接
            ChannelFuture future = bootstrap.connect(host, port).sync();
            // 等待服务端监听端口关闭 连接成功后将持续阻塞该线程不会执行到finally中去
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (worker != null) {
                //关闭工作线程
                worker.shutdownGracefully();
            }
        }
    }

}
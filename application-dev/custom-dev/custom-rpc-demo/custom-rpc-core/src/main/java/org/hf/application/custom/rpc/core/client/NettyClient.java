package org.hf.application.custom.rpc.core.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.hf.application.custom.rpc.core.base.RpcRequest;

public class NettyClient {

    /**
     * 消息通道
     */
    private Channel channel;

    /**
     * 客户端工作线程
     */
    private EventLoopGroup worker;

    public void start(String host, int port) {
        try {
            doStart(host, port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doStart(String host, int port) throws Exception {
        //定义工作线程组
        this.worker = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(this.worker)
                .channel(NioSocketChannel.class)
                .handler(new ClientInitializer());
        //连接到远程服务
        ChannelFuture future = bootstrap.connect(host, port).sync();
        //保留channel对象，方便后面通过该通道发送消息
        this.channel = future.channel();
    }

    /**
     * 关闭通道和工作线程组
     */
    public void close() {
        if (null != this.channel && this.channel.isActive()) {
            this.channel.close();
        }
        if (null != this.worker && !this.worker.isShutdown()) {
            this.worker.shutdownGracefully();
        }
    }

    /**
     * 发送消息
     * @param request 入参
     */
    public void sendMsg(RpcRequest request) {
//        this.channel.writeAndFlush(request);
        // 这里采用多线程的方式进行消息发送
        channel.eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                channel.writeAndFlush(request);
            }
        });

    }
}
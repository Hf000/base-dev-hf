package org.hf.application.netty.tcp.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hf.application.netty.tcp.client.handler.BaseClientHandler;
import org.hf.application.netty.tcp.client.utils.SpringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author hf
 * 自定义实现tcp消息客户端 - 1 客户端初始化处理
 */
@Slf4j
@Component
@Scope("prototype")
@Getter
@NoArgsConstructor
public class NettyClient implements Runnable {

    /**
     * netty绑定的端口号
     */
    @Value("${netty.server.port}")
    private int port;

    /**
     * netty绑定的地址
     */
    @Value("${netty.server.host}")
    private String host;

    /**
     * 客户端唯一标识
     */
    private String imei;

    /**
     * 自定义业务数据
     */
    private Map<String, Object> bizData;

    /**
     * netty工作线程对象
     */
    private EventLoopGroup workGroup;

    /**
     * 消息处理器对象
     */
    private Class<BaseClientHandler> clientHandlerClass;

    /**
     * Channel异步结果返回对象
     */
    private ChannelFuture channelFuture;

    public NettyClient(String imei, Map<String, Object> bizData, EventLoopGroup workGroup, Class<BaseClientHandler> clientHandlerClass) {
        this.imei = imei;
        this.bizData = bizData;
        this.workGroup = workGroup;
        this.clientHandlerClass = clientHandlerClass;
    }

    @Override
    public void run() {
        try {
            // 初始化客户端
            this.init();
            log.info("客户端启动imei={}", imei);
        } catch (Exception e) {
            log.error("客户端启动失败:{}", e.getMessage(), e);
        }
    }

    /**
     * 关闭客户端
     **/
    public void close() {
        if (null != this.channelFuture) {
            this.channelFuture.channel().close();
        }
        // 这里的工作线程组是初始化时已经创建好传入的,是统一管理的,所以这里不能进行关闭,否则会影响其他客户端
//        if (null != this.workGroup && !this.workGroup.isShutdown()) {
//            this.workGroup.shutdownGracefully();
//        }
        NettyClientHolder.get().remove(this.imei);
    }

    /**
     * 发送消息
     * @param message 消息内容
     */
    public void send(String message) {
        try {
            if (!this.channelFuture.channel().isActive()) {
                log.info("通道不活跃imei={}", this.imei);
                return;
            }
            if (!StringUtils.isEmpty(message)) {
                log.info("队列消息发送===>{}", message);
                this.channelFuture.channel().writeAndFlush(message);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 初始化客户端
     */
    private void init() throws Exception {
        // 将本实例传递到handler
        BaseClientHandler clientHandler = SpringUtils.getBean(clientHandlerClass, this);
        // 开启client客户端
        Bootstrap b = new Bootstrap();
        // 通过辅助类去构造client
        b.group(workGroup)
                // 设置客户端socket通道
                .channel(NioSocketChannel.class)
                // 配置参数
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                .option(ChannelOption.SO_RCVBUF, 1024 * 32)
                .option(ChannelOption.SO_SNDBUF, 1024 * 32)
                // 设置通过自定义初始化的方式加载handler, 这里可以设置多个数据处理器链路
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        // 解决粘包/拆包问题
                        ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024 * 1024, Unpooled.copiedBuffer("\r\n".getBytes())));
                        // String编码。
                        ch.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8));
                        // String解码。
                        ch.pipeline().addLast(new StringDecoder(CharsetUtil.UTF_8));
                        // 心跳设置
                        ch.pipeline().addLast(new IdleStateHandler(0, 0, 600, TimeUnit.SECONDS));
                        // 消息处理器
                        ch.pipeline().addLast(clientHandler);
                    }
                });
        this.connect(b);
    }

    /**
     * 处理客户端连接
     * @param b 客户端对象
     */
    private void connect(Bootstrap b) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        //重连2次
        final int maxRetries = 2;
        final AtomicInteger count = new AtomicInteger();
        final AtomicBoolean flag = new AtomicBoolean(false);
        try {
            this.channelFuture = b.connect(host, port).addListener(
                    new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture future) {
                            if (!future.isSuccess()) {
                                if (count.incrementAndGet() > maxRetries) {
                                    log.warn("imei={}重连超过{}次", imei, maxRetries);
                                } else {
                                    log.info("imei={}重连第{}次", imei, count);
                                    b.connect(host, port).addListener(this);
                                }
                            } else {
                                log.info("imei={}连接成功,连接IP:{}连接端口:{}", imei, host, port);
                                flag.set(true);
                            }
                        }
                    }).sync(); //同步连接
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        log.info("设备imei={}，channelId={} 连接耗时={}ms", imei, channelFuture.channel().id(), System.currentTimeMillis() - startTime);
        if (flag.get()) {
            // 连接成功后将持续阻塞该线程
            channelFuture.channel().closeFuture().sync();
        }
    }

}
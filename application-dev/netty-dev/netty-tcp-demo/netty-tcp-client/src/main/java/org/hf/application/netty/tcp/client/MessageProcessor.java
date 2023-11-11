package org.hf.application.netty.tcp.client;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;
import org.hf.application.netty.tcp.client.handler.CustomClientHandler;
import org.hf.application.netty.tcp.client.pojo.NettyMsgModel;
import org.hf.application.netty.tcp.client.redis.RedisCache;
import org.hf.application.netty.tcp.client.utils.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author hf
 * 自定义实现tcp消息客户端 - 4 消息处理
 */
@Slf4j
@Component
public class MessageProcessor {

    @Autowired
    private RedisCache redisCache;

    /**
     * 工作线程数
     */
    @Value("${netty.client.worker-thread}")
    private Integer workerThread;

    /**
     * 消息队列锁前缀
     */
    public static final String NETTY_QUEUE_LOCK = "nettyQueueLock:";

    /**
     * 线程池变量
     */
    private ExecutorService executor;

    @PostConstruct
    public void init() {
        // 初始化线程池
        this.executor = this.createDefaultExecutorService(10);
    }

    /**
     * 客户端阻塞的线程池
     * @param size 核心线程数 建议大于需要创建的客户端数量
     */
    private ExecutorService createDefaultExecutorService(int size) {
        // 创建线程池
        return new ThreadPoolExecutor(size, size * 2, 300L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1), new ThreadFactoryImpl("Demo_NettyClientThread_", false));
    }

    /**
     * 消息处理
     * @param nettyMsgModel 消息
     */
    public void process(NettyMsgModel nettyMsgModel) {
        String imei = nettyMsgModel.getImei();
        try {
            // 为避免收到同一台设备多条消息后重复创建客户端，必须加锁
            synchronized (this) {
                // 上一条消息处理中
                if (redisCache.hasKey(NETTY_QUEUE_LOCK + imei)) {
                    log.info("imei={} 消息处理中，重新入列", imei);
                    // 放回队列重新等待消费 延迟x秒（实际项目中应该使用rocketmq或者rabbitmq实现延迟消费）
                    this.putDelay(nettyMsgModel);
                    log.info("imei={} 消息处理中，重新入列完成", imei);
                    return;
                } else {
                    // 如果没有在连接中的直接加锁
                    redisCache.setCacheObject(NETTY_QUEUE_LOCK + imei, "1", 120, TimeUnit.SECONDS);
                }
            }
            // 缓存中存在证明客户端连接正常则发送消息
            if (NettyClientHolder.get().containsKey(imei)) {
                // 根据身份标识或读取对应的客户端连接
                NettyClient nettyClient = NettyClientHolder.get().get(imei);
                // 通道活跃直接发送消息
                if (null != nettyClient.getChannelFuture() && nettyClient.getChannelFuture().channel().isActive()) {
                    if (!nettyClient.getChannelFuture().channel().isWritable()) {
                        log.warn("警告，通道不可写，imei={}，channelId={}", nettyClient.getImei(),
                                nettyClient.getChannelFuture().channel().id());
                    }
                    nettyClient.send(nettyMsgModel.getMsg());
                } else {
                    log.info("client imei={}，通道不活跃，主动关闭", nettyClient.getImei());
                    nettyClient.close();
                    // 重新创建客户端发送
                    this.createClientAndSend(nettyMsgModel);
                }
            } else {
                // 缓存中不存在则创建新的客户端
                this.createClientAndSend(nettyMsgModel);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            // 执行完后解锁
            redisCache.deleteObject(NETTY_QUEUE_LOCK + imei);
        }

    }

    /**
     * 创建netty客户端
     * @param nettyMsgModel 消息
     */
    private void createClientAndSend(NettyMsgModel nettyMsgModel) {
        log.info("创建客户端执行中imei={}", nettyMsgModel.getImei());
        // 此处的CustomClientHandler可以根据自己的业务定义
        NettyClient nettyClient = SpringUtils.getBean(NettyClient.class, nettyMsgModel.getImei(), nettyMsgModel.getBizData(),
                this.createDefaultWorkGroup(this.workerThread), CustomClientHandler.class);
        // 执行客户端初始化
        executor.execute(nettyClient);
        try {
            // 利用锁等待客户端激活
            synchronized (nettyClient) {
                // 因为客户端创建后使用的是线程池执行初始化, 是异步执行, 所以这里需要等待一会再发消息
                long c1 = System.currentTimeMillis();
                // 最多阻塞5秒 5秒后客户端仍然未激活则自动解锁 这里视实际情况而定,因为不合理的等待可能导致线程池耗尽或是内存溢出, 客户端创建成功后会调notify()进行唤醒
                nettyClient.wait(5000);
                long c2 = System.currentTimeMillis();
                log.info("创建客户端wait耗时={}ms", c2 - c1);
            }
            // 连接成功
            if (null != nettyClient.getChannelFuture() && nettyClient.getChannelFuture().channel().isActive()) {
                // 存入缓存
                NettyClientHolder.get().put(nettyMsgModel.getImei(), nettyClient);
                // 客户端激活后发送消息
                nettyClient.send(nettyMsgModel.getMsg());
            } else {
                // 连接失败
                log.warn("客户端创建失败，imei={}", nettyMsgModel.getImei());
                nettyClient.close();
                //可以把消息重新入列处理
            }
        } catch (Exception e) {
            log.error("客户端初始化发送消息异常===>{}", e.getMessage(), e);
        }
    }

    /**
     * 所有客户端共用的工作线程
     *
     * @param threadSize 线程数
     */
    private EventLoopGroup createDefaultWorkGroup(int threadSize) {
        return new NioEventLoopGroup(threadSize, new ThreadFactoryImpl("Custom_NettyWorkGroupThread_", false));
    }

    /**
     * 延迟放入消息
     * @param nettyMsgModel 消息对象
     */
    private void putDelay(NettyMsgModel nettyMsgModel) {
        // 创建定时器对象
        Timer timer = new Timer();
        // 延迟3秒，执行一次task,将消息放入消息队列
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                QueueHolder.get().offer(nettyMsgModel);
                // 终止timer线程
                timer.cancel();
            }
        }, 3000);
    }

}
package org.hf.application.netty.tcp.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.hf.application.netty.tcp.client.NettyClient;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author hf
 * 自定义实现tcp消息客户端 - 3 消息处理器抽象实现
 */
@Slf4j
@Component
@Scope("prototype")
public class CustomClientHandler extends BaseClientHandler {

    /**
     * 身份标识
     */
    private final String imei;

    /**
     * 业务数据对象集合
     */
    private final Map<String, Object> bizData;

    /**
     * netty客户端对象
     */
    private final NettyClient nettyClient;

    /**
     * 记录闲时读写操作次数
     */
    private int allIdleCounter = 0;

    /**
     * 最大闲时读写操作次数
     */
    private static final int MAX_IDLE_TIMES = 3;

    public CustomClientHandler(NettyClient nettyClient) {
        this.nettyClient = nettyClient;
        this.imei = nettyClient.getImei();
        this.bizData = nettyClient.getBizData();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("客户端imei={}，通道激活成功", this.imei);
        // 当通道激活后解锁队列线程，然后再发送消息
        synchronized (this.nettyClient) {
            // 唤醒等待的netty客户端线程, 由于在消息分发处理时创建客户端时进行了加锁等待,所以创建成功后这里进行唤醒
            this.nettyClient.notify();
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.warn("客户端imei={}，通道断开连接", this.imei);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        log.info("客户端imei={}，收到消息:  {}", this.imei, msg);
        // 处理业务...
        if ("shutdown".equals(msg)) {
            // 关闭连接
            this.nettyClient.close();
        }
    }

    /**
     * 处理客户端与服务端建立连接后的自定义事件, 这里是处理心跳事件
     * @param ctx    通道处理器上下文
     * @param evt    事件对象
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            boolean flag = false;
            if (e.state() == IdleState.ALL_IDLE) {
                this.allIdleCounter++;
                log.info("客户端imei={}触发闲读或写第{}次", this.imei, this.allIdleCounter);
                if (this.allIdleCounter >= MAX_IDLE_TIMES) {
                    flag = true;
                }
            }
            if (flag) {
                log.warn("读写超时达到{}次，主动断开连接", MAX_IDLE_TIMES);
                ctx.channel().close();
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("客户端imei={}，连接异常{}", imei, cause.getMessage(), cause);
    }
}
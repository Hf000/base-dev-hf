package org.hf.application.netty.tcp.client;

import org.hf.application.netty.tcp.client.pojo.NettyMsgModel;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author hf
 * 自定义实现tcp消息客户端 - 7 阻塞消息队列 (实际生产中应该使用MQ消息中间件代替)
 */
public class QueueHolder {

    private static final ArrayBlockingQueue<NettyMsgModel> QUEUE = new ArrayBlockingQueue<>(100);

    public static ArrayBlockingQueue<NettyMsgModel> get() {
        return QUEUE;
    }
}
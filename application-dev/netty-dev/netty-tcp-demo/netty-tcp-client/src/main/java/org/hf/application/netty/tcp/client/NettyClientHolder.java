package org.hf.application.netty.tcp.client;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hf
 * 自定义实现tcp消息客户端 - 6 netty客户端持有对象
 */
public class NettyClientHolder {

    /**
     * 由于netty的通道无法序列化，因此不能存入redis，只能缓存在本地内存中
     */
    private static final ConcurrentHashMap<String, NettyClient> CLIENT_MAP = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, NettyClient> get() {
        return CLIENT_MAP;
    }

}
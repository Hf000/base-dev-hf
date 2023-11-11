package org.hf.application.netty.tcp.server;

import io.netty.channel.ChannelHandlerContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hf
 * 自定义实现tcp消息服务端 - 3 服务端通道处理器上下文持有对象
 */
public class ChannelHandlerContextHolder {

    private static final Map<Integer, ChannelHandlerContext> MAP = new ConcurrentHashMap<>();

    public static ChannelHandlerContext getChannelCtx(Integer id) {
        return MAP.getOrDefault(id, null);
    }

    public static void updateMap(Integer id, ChannelHandlerContext ctx) {
        MAP.put(id, ctx);
    }

    public static void removeByKey(Integer id) {
        ChannelHandlerContext channelHandlerContext = MAP.get(id);
        MAP.remove(id);
        if (channelHandlerContext != null) {
            channelHandlerContext.close();
        }
    }

    public static void removeByCtx(ChannelHandlerContext ctx) {
        for (Map.Entry<Integer, ChannelHandlerContext> entry : MAP.entrySet()) {
            if (entry.getValue().equals(ctx)) {
                MAP.remove(entry.getKey());
                break;
            }
        }
    }

    public static int getChannelLength() {
        return MAP.size();
    }
}
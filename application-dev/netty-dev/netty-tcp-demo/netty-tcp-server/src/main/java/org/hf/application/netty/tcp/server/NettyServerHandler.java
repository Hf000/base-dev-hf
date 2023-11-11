package org.hf.application.netty.tcp.server;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

/**
 * @author hf
 * 自定义实现tcp消息服务端 - 2 消息处理器
 */
@Slf4j
@Scope("prototype")
@Component
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    private int idleCounter = 0;

    /**
     * 业务数据处理
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            idleCounter = 0;
            ByteBuf in = (ByteBuf) msg;
            int readableBytes = in.readableBytes();
            byte[] bytes = new byte[readableBytes];
            in.readBytes(bytes);
            String msgStr = new String(bytes, StandardCharsets.UTF_8);
            log.info("服务端接受的消息 : " + msgStr);
            JSONObject jsonObject = JSON.parseObject(msgStr);
            if ("up".equals(jsonObject.getString("data"))) {
                log.info("设备{}上线了", jsonObject.getInteger("id"));
                ChannelHandlerContextHolder.updateMap(jsonObject.getInteger("id"), ctx);
            } else {
                log.info("设备上送信息{}", jsonObject.getString("data"));
            }
            // 向客户端响应消息
            if ("shutdown".equals(msgStr)) {
                ctx.writeAndFlush(Unpooled.copiedBuffer("shutdown\r\n".getBytes()));
            } else {
                ctx.writeAndFlush(Unpooled.copiedBuffer("success\r\n".getBytes()));
            }
        } finally {
            // 抛弃收到的数据,手动释放资源
            ReferenceCountUtil.release(msg);
        }

    }

    /**
     * 从客户端收到新的数据、读取完成---调用
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        log.info("从客户端收到新的数据读取完成********");
        if (ctx != null) {
            ctx.flush();
        }
    }

    /**
     * 客户端与服务端建立连接--执行
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        ctx.channel().read();
        String clientIp = this.getClientIp(ctx);
        // 此处不能使用ctx.close()，否则客户端始终无法与服务端建立连接
        log.info("客户端与服务端建立连接:{}", clientIp);
    }


    /**
     * 客户端与服务端断连-调用
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        String clientIp = this.getClientIp(ctx);
        //断开连接时，服务端关闭连接，避免造成资源浪费
        ctx.close();
        log.info("客户端与服务端断连:{}", clientIp);
    }

    /**
     * 当 Netty 由于 IO 错误或者处理器在处理事件时抛出的异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if (ctx != null) {
            ChannelHandlerContextHolder.removeByCtx(ctx);
            //抛出异常，断开与客户端的连接
            ctx.close();
            log.error("连接异常，服务端主动断开连接{}", cause.getMessage(), cause);
        }

    }

    /**
     * 服务端read超时-调用
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        String clientIp = this.getClientIp(ctx);
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state().equals(IdleState.WRITER_IDLE)) {
                log.warn("客户端写超时:{}", clientIp);
                idleCounter++;
                if (idleCounter > 3) {
                    log.warn("客户端写超时超过3次，断开");
                    ctx.disconnect();
                }
            }
        }
    }

    /**
     * 获取连接地址
     */
    private String getClientIp(ChannelHandlerContext ctx) {
        InetSocketAddress inSocket = (InetSocketAddress) ctx.channel().remoteAddress();
        return inSocket.getAddress().getHostAddress();
    }

}
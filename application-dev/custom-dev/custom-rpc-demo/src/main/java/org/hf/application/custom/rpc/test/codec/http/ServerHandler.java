package org.hf.application.custom.rpc.test.codec.http;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

import java.util.Map;

public class ServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Override
    public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {

        //解析FullHttpRequest，得到请求参数
        Map<String, String> paramMap = new RequestParser(request).parse();
        String name = paramMap.get("name");

        //构造响应对象
        FullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=utf-8");

        StringBuilder sb = new StringBuilder();
        sb.append("<h1>");
        sb.append("你好，" + name);
        sb.append("</h1>");

        httpResponse.content().writeBytes(Unpooled.copiedBuffer(sb, CharsetUtil.UTF_8));

        ctx.writeAndFlush(httpResponse)
                .addListener(ChannelFutureListener.CLOSE); //操作完成后，将channel关闭
    }

}
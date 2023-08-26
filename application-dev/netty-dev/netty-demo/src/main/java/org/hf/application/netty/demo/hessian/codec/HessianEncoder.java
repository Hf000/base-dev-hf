package org.hf.application.netty.demo.hessian.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 编码
 */
public class HessianEncoder extends MessageToByteEncoder<User> {

    private final HessianSerializer hessianSerializer = new HessianSerializer();

    @Override
    protected void encode(ChannelHandlerContext ctx, User msg, ByteBuf out) throws Exception {
        byte[] bytes = hessianSerializer.serialize(msg);
        out.writeBytes(bytes);
    }
}
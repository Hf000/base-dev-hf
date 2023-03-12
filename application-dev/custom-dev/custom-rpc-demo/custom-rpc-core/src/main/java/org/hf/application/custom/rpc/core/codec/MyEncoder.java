package org.hf.application.custom.rpc.core.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.hf.application.custom.rpc.core.base.BaseRpcBean;
import org.hf.application.custom.rpc.core.util.HessianSerializer;
import org.hf.application.custom.rpc.core.util.MySerializer;

/**
 * 编码器
 */
public class MyEncoder extends MessageToByteEncoder<BaseRpcBean> {


    private static MySerializer hessianSerializer = new HessianSerializer();

    @Override
    protected void encode(ChannelHandlerContext ctx, BaseRpcBean msg, ByteBuf out) throws Exception {
        byte[] bytes = hessianSerializer.serialize(msg);

        out.writeInt(bytes.length);
        out.writeBytes(bytes);
    }
}

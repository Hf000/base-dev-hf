package org.hf.application.custom.rpc.core.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import org.hf.application.custom.rpc.core.base.BaseRpcBean;
import org.hf.application.custom.rpc.core.util.HessianSerializer;
import org.hf.application.custom.rpc.core.util.MySerializer;

import java.util.List;

/**
 * 解码器
 */
public class MyDecoder<T extends BaseRpcBean> extends ReplayingDecoder<Void> {

    private static final MySerializer HESSIAN_SERIALIZER = new HessianSerializer();

    private final Class<T> clazz;

    public MyDecoder(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 获取消息内容
        byte[] bytes = new byte[in.readInt()];
        in.readBytes(bytes);
        // 进行反序列化
        BaseRpcBean rpcBean = HESSIAN_SERIALIZER.deserialize(bytes, clazz);
        out.add(rpcBean);
    }
}
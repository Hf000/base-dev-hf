package org.hf.application.netty.bytebuffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

/**
 * 重置读/写的索引为0
 * 通过clear() 重置readerIndex 、 writerIndex 为0，需要注意的是，重置并没有删除真正的内容
 */
public class TestByteBufClear {
    public static void main(String[] args) {
        ByteBuf byteBuf = Unpooled.copiedBuffer("hello world", CharsetUtil.UTF_8);

        System.out.println("byteBuf的容量为：" + byteBuf.capacity());
        System.out.println("byteBuf的可读容量为：" + byteBuf.readableBytes());
        System.out.println("byteBuf的可写容量为：" + byteBuf.writableBytes());

        //重置readerIndex 、 writerIndex 为0
        byteBuf.clear();

        System.out.println("byteBuf的容量为：" + byteBuf.capacity());
        System.out.println("byteBuf的可读容量为：" + byteBuf.readableBytes());
        System.out.println("byteBuf的可写容量为：" + byteBuf.writableBytes());
    }
}

package org.hf.application.netty.bytebuffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

/**
 * 丢弃已读字节
 * 通过discardReadBytes()⽅可以将已经读取的数据进⾏丢弃处理，就可以回收已经读取的字节空间
 */
public class TestByteBufDiscard {
    public static void main(String[] args) {
        ByteBuf byteBuf = Unpooled.copiedBuffer("hello world", CharsetUtil.UTF_8);

        System.out.println("byteBuf的容量为：" + byteBuf.capacity());
        System.out.println("byteBuf的可读容量为：" + byteBuf.readableBytes());
        System.out.println("byteBuf的可写容量为：" + byteBuf.writableBytes());

        while (byteBuf.isReadable()){
            System.out.println((char)byteBuf.readByte());
        }

        //丢弃已读的字节空间
        byteBuf.discardReadBytes();

        System.out.println("byteBuf的容量为：" + byteBuf.capacity());
        System.out.println("byteBuf的可读容量为：" + byteBuf.readableBytes());
        System.out.println("byteBuf的可写容量为：" + byteBuf.writableBytes());
    }
}
package org.hf.application.custom.rpc.test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class TestByteBuf02 {

    public static void main(String[] args) {

        //构造空的字节缓冲区，初始大小为10，最大为20
        ByteBuf byteBuf = Unpooled.buffer(10,20);

        System.out.println("byteBuf的容量为：" + byteBuf.capacity());
        System.out.println("byteBuf的可读容量为：" + byteBuf.readableBytes());
        System.out.println("byteBuf的可写容量为：" + byteBuf.writableBytes());

        for (int i = 0; i < 5; i++) {
            byteBuf.writeInt(i); //写入int类型，一个int占4个字节
        }
        System.out.println("ok");

        System.out.println("byteBuf的容量为：" + byteBuf.capacity());
        System.out.println("byteBuf的可读容量为：" + byteBuf.readableBytes());
        System.out.println("byteBuf的可写容量为：" + byteBuf.writableBytes());

        while (byteBuf.isReadable()){
            System.out.println(byteBuf.readInt());
        }

    }
}
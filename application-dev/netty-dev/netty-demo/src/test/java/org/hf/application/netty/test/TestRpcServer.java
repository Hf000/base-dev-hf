package org.hf.application.netty.test;

import org.hf.application.netty.server.MyRpcServer;
import org.junit.Test;

/**
 * <p> rpc服务端测试 </p>
 * @author hufei
 * @date 2022/8/6 20:44
*/
public class TestRpcServer {

    @Test
    public void testServer() {
        // 默认使⽤的是DirectByteBuf直接缓冲区，如果需要使⽤HeapByteBuf堆缓冲区模式，则需要进⾏系统参数的设置，默认使用的是直接缓冲区，那么handler中一定要进行资源释放，否则容易导致内存泄漏
        // 设置为非池化方式一： netty中IO操作都是基于Unsafe完成的， 搭配serverBootstrap.childOption(ChannelOption.ALLOCATOR, UnpooledByteBufAllocator.DEFAULT);使用
//        System.setProperty("io.netty.noUnsafe", "true");
        /* Netty 提供了两种 ByteBufAllocator 的实现，分别是：
           PooledByteBufAllocator，实现了 ByteBuf 的对象的池化，提⾼性能减少并最⼤限度地减少内存碎⽚。
           UnpooledByteBufAllocator，没有实现对象的池化，每次会⽣成新的对象实例。
           Netty默认使⽤了PooledByteBufAllocator
         */
        // 设置为池化， 默认方式
//        System.setProperty("io.netty.allocator.type", "pooled");
        // 设置为非池化方式二
//        System.setProperty("io.netty.allocator.type", "unpooled");
        MyRpcServer myRpcServer = new MyRpcServer();
        myRpcServer.start(9527);
    }

}

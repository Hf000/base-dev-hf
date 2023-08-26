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
        // 默认使⽤的是DirectByteBuf，如果需要使⽤HeapByteBuf模式，则需要进⾏系统参数的设置
        // netty中IO操作都是基于Unsafe完成的
//        System.setProperty("io.netty.noUnsafe", "true");
        /* Netty 提供了两种 ByteBufAllocator 的实现，分别是：
           PooledByteBufAllocator，实现了 ByteBuf 的对象的池化，提⾼性能减少并最⼤限度地减少内存碎⽚。
           UnpooledByteBufAllocator，没有实现对象的池化，每次会⽣成新的对象实例。
           Netty默认使⽤了PooledByteBufAllocator
         */
//        System.setProperty("io.netty.allocator.type", "pooled");
//        System.setProperty("io.netty.allocator.type", "unpooled");
        MyRpcServer myRpcServer = new MyRpcServer();
        myRpcServer.start(9527);
    }

}

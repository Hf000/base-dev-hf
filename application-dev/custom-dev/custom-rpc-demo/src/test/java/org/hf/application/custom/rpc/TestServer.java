package org.hf.application.custom.rpc;

import org.hf.application.custom.rpc.server.MyRPCServer;
import org.junit.Test;

public class TestServer {

    @Test
    public void testServer() throws Exception{
        System.setProperty("io.netty.noUnsafe", "true");

//        System.setProperty("io.netty.allocator.type", "unpooled");

        MyRPCServer myRPCServer = new MyRPCServer();

        myRPCServer.start(5566);
    }

}
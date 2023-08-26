package org.hf.application.custom.rpc.order;

import org.hf.application.custom.rpc.core.server.NettyServer;

public class MyServer {

    public static void main(String[] args) {
        NettyServer.start("127.0.0.1", 5566);
    }
}

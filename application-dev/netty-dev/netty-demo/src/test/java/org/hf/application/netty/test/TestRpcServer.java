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
        MyRpcServer myRpcServer = new MyRpcServer();
        myRpcServer.start(9527);
    }

}

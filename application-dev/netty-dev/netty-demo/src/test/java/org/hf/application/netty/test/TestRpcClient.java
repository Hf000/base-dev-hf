package org.hf.application.netty.test;

import org.hf.application.netty.client.MyRpcClient;
import org.junit.Test;

/**
 * <p> rpc客户端测试 </p>
 * @author hufei
 * @date 2022/8/6 20:44
*/
public class TestRpcClient {

    @Test
    public void testClient() {
        MyRpcClient client = new MyRpcClient();
        client.start("127.0.0.1", 9527);
    }

}

package org.hf.application.custom.rpc;

import org.hf.application.custom.rpc.client.MyRPCClient;
import org.junit.Test;

public class TestClient {

    @Test
    public void testClient() throws Exception{
        new MyRPCClient().start("127.0.0.1", 5566);
    }
}
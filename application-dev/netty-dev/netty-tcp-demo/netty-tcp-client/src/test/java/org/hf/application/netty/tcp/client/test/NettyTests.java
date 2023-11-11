package org.hf.application.netty.tcp.client.test;

import lombok.extern.slf4j.Slf4j;
import org.hf.application.netty.tcp.client.QueueHolder;
import org.hf.application.netty.tcp.client.pojo.NettyMsgModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author hf
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class NettyTests {

    /**
     * 模拟发两次间隔的消息
     */
    @Test
    public void testMsg() {
        QueueHolder.get().offer(NettyMsgModel.create("87655421","Hello World!"));
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        QueueHolder.get().offer(NettyMsgModel.create("87655421","Hello World Too!"));
    }
}
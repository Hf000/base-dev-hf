package org.hf.application.netty.tcp.client.controller;

import org.hf.application.netty.tcp.client.QueueHolder;
import org.hf.application.netty.tcp.client.pojo.NettyMsgModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hf
 * 测试接口
 */
@RestController
@RequestMapping("/netty/msg/demo")
public class DemoController {

    /**
     * 间隔发送两条消息
     */
    @GetMapping("testOne")
    public void testOne() {
        QueueHolder.get().offer(NettyMsgModel.create("87654321", "Hello World!"));
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        QueueHolder.get().offer(NettyMsgModel.create("87654321", "Hello World Too!"));
    }

    /**
     * 任意发送消息
     *
     * @param imei 身份标识
     * @param msg   消息内容
     */
    @GetMapping("testTwo")
    public void testTwo(@RequestParam String imei, @RequestParam String msg) {
        QueueHolder.get().offer(NettyMsgModel.create(imei, msg));
    }

    /**
     * 连续发送两条消息 第二条由于redis锁将会重新放回队列延迟消费
     */
    @GetMapping("testThree")
    public void testThree() {
        QueueHolder.get().offer(NettyMsgModel.create("12345678", "Hello World!"));
        QueueHolder.get().offer(NettyMsgModel.create("12345678", "Hello World Too!"));
    }
}
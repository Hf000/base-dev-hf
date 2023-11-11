package org.hf.application.netty.tcp;

import lombok.extern.slf4j.Slf4j;
import org.hf.application.netty.tcp.server.NettyServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author hf
 * CommandLineRunner springboot项目在启动时需要预先加载的数据需要实现此接口
 * 自定义实现tcp消息服务端 - 4 服务端启动
 */
@Slf4j
@SpringBootApplication
public class NettyServerApplication implements CommandLineRunner {

    @Autowired
    private NettyServer nettyServer;

    @Override
    public void run(String... args) {
        // 需要在程序启动时执行的逻辑, 启动netty服务端
        nettyServer.start();
    }

    public static void main(String[] args) {
        SpringApplication.run(NettyServerApplication.class,args);
    }
}
package org.hf.application.netty.tcp;

import lombok.extern.slf4j.Slf4j;
import org.hf.application.netty.tcp.client.MessageProcessor;
import org.hf.application.netty.tcp.client.QueueHolder;
import org.hf.application.netty.tcp.client.ThreadFactoryImpl;
import org.hf.application.netty.tcp.client.pojo.NettyMsgModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author hf
 * CommandLineRunner springboot项目在启动时需要预先加载的数据需要实现此接口,
 * 注意这里需要注入spring管理的bean, 所以需要加上@Configuration注解   // TODO 待验证
 * 自定义实现tcp消息客户端 - 8 消息消费
 */
@Slf4j
@Configuration
@SpringBootApplication
public class NettyClientApplication implements CommandLineRunner {

    @Autowired
    private MessageProcessor messageProcessor;

    private static final Integer MAIN_THREAD_POOL_SIZE = 4;

    public static void main(String[] args) {
        SpringApplication.run(NettyClientApplication.class, args);
    }

    /**
     * 创建固定线程数的线程池, 自定义线程工厂创建线程
     */
    private final ExecutorService executor = Executors.newFixedThreadPool(MAIN_THREAD_POOL_SIZE,
            new ThreadFactoryImpl("Custom_Thread_", false));

    @Override
    public void run(String... args) {
        // 需要在程序启动时执行的逻辑, 创建线程消费消息
        Thread loopThread = new Thread(new LoopThread());
        loopThread.start();
    }

    public class LoopThread implements Runnable {
        @Override
        public void run() {
            // 不停循环去消息队列里取消息消费, 可以考虑通过消费MQ消息去替代这里从本地队列中取消息的逻辑
            for (int i = 0; i < MAIN_THREAD_POOL_SIZE; i++) {
                executor.execute(() -> {
                    //noinspection InfiniteLoopStatement
                    while (true) {
                        try {
                            // 取走BlockingQueue里排在首位的对象,若BlockingQueue为空,阻断进入等待状态直到
                            NettyMsgModel nettyMsgModel = QueueHolder.get().take();
                            messageProcessor.process(nettyMsgModel);
                        } catch (InterruptedException e) {
                            log.error(e.getMessage(), e);
                        }
                    }
                });
            }
        }
    }
}
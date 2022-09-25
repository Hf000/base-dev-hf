package org.hf.boot.springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * <p> 线程池 </p >
 * @author hufei
 * @date 2022-09-22
 **/
@Configuration
@EnableAsync
public class AsyncExecutorConfig {
    /**
     * 核心线程数（ 默认线程数）
     */
    private static final int CORE_POOL_SIZE  = 5;
    /**
     * 最大线程数
     */
    private static final int MAX_POOL_SIZE = 20;
    /**
     * 允许线程空闲时间（单位：默认为秒）
     */
    private static final int KEEP_ALIVE_TIME = 100;
    /**
     * 缓冲队列数
     */
    private static final int QUEUE_CAPACITY = 10000;

    /**
     * bean的名称，默认为首字母小写的方法名
     */
    @Bean("customTaskExecutor")
    public ThreadPoolTaskExecutor jvsTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setKeepAliveSeconds(KEEP_ALIVE_TIME);
        executor.setThreadNamePrefix("customTaskExecutor-");
        // 线程池对拒绝任务的处理策略:丢弃最新的
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        // 初始化
        executor.initialize();
        return executor;
    }

}

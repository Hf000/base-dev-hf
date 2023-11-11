package org.hf.application.netty.tcp.client;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author hf
 * 自定义线程工厂实现
 */
public class ThreadFactoryImpl implements ThreadFactory {
    /**
     * 线程索引
     */
    private final AtomicLong threadIndex = new AtomicLong(0);
    /**
     * 线程名称前缀
     */
    private final String threadNamePrefix;
    /**
     * 是否设置为守护线程
     */
    private final boolean daemon;

    public ThreadFactoryImpl(final String threadNamePrefix) {
        this(threadNamePrefix, false);
    }

    public ThreadFactoryImpl(final String threadNamePrefix, boolean daemon) {
        this.threadNamePrefix = threadNamePrefix;
        this.daemon = daemon;
    }

    @Override
    public Thread newThread(@NotNull Runnable runnable) {
        // 创建线程 参数1:Runnable对象,线程执行的run()逻辑; 参数2:执行线程名称
        Thread thread = new Thread(runnable, threadNamePrefix + this.threadIndex.incrementAndGet());
        // daemon=true,则将线程设置为守护线程, 守护线程特征: 可以自动结束自己的声明周期, 而非守护线程不可以.
        thread.setDaemon(daemon);
        return thread;
    }
}
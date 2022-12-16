package org.hf.application.javabase.utils;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <p> 线程池工具类 </p >
 *
 * @author hufei
 * @version 1.0.0
 * @date 2022/8/21 13:24
 */
@SuppressWarnings("all")
public class ThreadPoolUtil {

    private static final int CORE_POOL_SIZE = 5;
    private static final int MAX_POOL_SIZE = 10;
    private static final int KEEP_ALIVE_TIME = 60;
    private static final int QUEUE_SIZE = 20;

    /**
     * 创建JDK线程池
     * @return ExecutorService
     */
    public static ExecutorService getJdkThreadPool() {
        ExecutorService executor = null;
        /* Executors工具创建线程池的四种方式
            1）newCachedThreadPool(): 弹性线程数
            2）newFixedThreadPool(int nThreads): 固定线程数
            3）newSingleThreadExecutor(): 单一线程数
            4）newScheduledThreadPool(int corePoolSize): 可调度，常用于定时
        */
        // 创建jdk线程池 阿里规范手册不推荐使用Executor工具类创建,容易造成OOM问题; 参数:线程数
//        executor = Executors.newFixedThreadPool(MAX_POOL_SIZE);
        // 手动创建jdk线程池; 参数: 1.核心线程数,2.最大线程数,3.线程存活时间,4.时间单位,5.提交任务队列,6.线程工厂:格式化线程名称,
        // 7.拒绝策略:超出提交任务队列的最大值则执行策略,这里的策略是直接丢弃不抛出异常
        // google的guava工具类创建线程工厂
//        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("custom-name").build();
        // 通过hutool工具类创建线程工厂
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNamePrefix("custom-name").build();
        executor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(QUEUE_SIZE), threadFactory, new ThreadPoolExecutor.DiscardPolicy());
        return executor;
    }

    /**
     * 关闭spring封装线程池
     * @param executor 线程池对象
     */
    public static void closeJdkThreadPool(ExecutorService executor) {
        if (executor == null) {
            return;
        }
        //  停止接收新任务,执行完已提交的任务(包括正在执行的和队列中等待的任务)
        executor.shutdown();
        // 停止接收新任务,停止已经提交的任务(包括正在执行(尝试中断)和队列中等待(忽略)的任务),返回未执行的任务列表
//        List<Runnable> runnables = executor.shutdownNow();
        // 阻塞当前线程,入参是超时时间和时间单位;可以继续提交任务,等待已提交的任务(包括正在执行和队列中等待的任务)执行完成,直到超时或者线程被中断
        /*try {
            boolean b = executor.awaitTermination(100, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }

    /**
     * 创建spring封装之后的线程池
     * @return ThreadPoolTaskExecutor
     */
    public static ThreadPoolTaskExecutor getSpringThreadPool() {
        // 创建spring封装的线程池对象
        ThreadPoolTaskExecutor threadPool = new ThreadPoolTaskExecutor();
        threadPool.setCorePoolSize(CORE_POOL_SIZE);
        threadPool.setMaxPoolSize(MAX_POOL_SIZE);
        threadPool.setKeepAliveSeconds(KEEP_ALIVE_TIME);
        threadPool.setQueueCapacity(QUEUE_SIZE);
        threadPool.setThreadNamePrefix("custom-name");
        // 线程池拒绝任务的处理策略: 丢弃最新的
        threadPool.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        // 初始化
        threadPool.initialize();
        return threadPool;
    }

    /**
     * 关闭spring封装线程池
     * @param threadPool 线程池对象
     */
    public static void closeSpringThreadPool(ThreadPoolTaskExecutor threadPool) {
        if (threadPool == null) {
            return;
        }
        // 实际调的是shutdown()方法
        threadPool.destroy();
    }

    /**
     * 线程休眠
     * @param time 休眠时间, 单位:秒
     */
    public static void threadSleep(long time) {
        // 让当前线程休眠, 此两种方式均可
        try {
            TimeUnit.SECONDS.sleep(time);
//            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
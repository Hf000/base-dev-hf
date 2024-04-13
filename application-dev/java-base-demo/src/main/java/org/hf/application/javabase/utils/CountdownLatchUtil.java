package org.hf.application.javabase.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * 闭锁工具类型
 */
@Slf4j
public class CountdownLatchUtil {
    
    /**
     * 执行任务,监听返回结果要用callable
     * @param callable 任务接口
     * @param executor 执行任务的线程池
     * @return 任务
     */
    public static <T> Future<T> asyncTaskHandler(Callable<T> callable, ThreadPoolTaskExecutor executor) {
        try {
            return executor.submit(callable);
        } catch (Exception e) {
            throw new RuntimeException("异步任务提交异常", e);
        }
    }
    
    /**
     * 获取执行结果
     * @param future future对象
     * @return 任务结果
     */
    public static <T> T fetchTaskResult(Future<T> future) {
        try {
            return future.get();
        } catch (Exception e) {
            throw new RuntimeException("获取异步任务执行结果异常", e);
        }
    }

    /**
     * 执行任务, 无返回结果
     * @param runnable 任务接口
     * @param executor 执行任务的线程池
     */
    public static void asyncTaskHandler(Runnable runnable, ThreadPoolTaskExecutor executor) {
        try {
            executor.execute(runnable);
        } catch (Exception e) {
            throw new RuntimeException("异步任务执行异常", e);
        }
    }
}
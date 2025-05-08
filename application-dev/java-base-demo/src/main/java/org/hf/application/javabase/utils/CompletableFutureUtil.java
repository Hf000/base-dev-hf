package org.hf.application.javabase.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * CompletableFuture异步线程框架工具类
 */
@Slf4j
public class CompletableFutureUtil {

    /**
     * 所有任务都正常执行完成后才会执行,无返回值, 如果有任务执行异常, 获取结果时会抛出异常
     * @param futures   异步执行业务对象集合
     */
    public static void blockWaitingUntilFinished(List<CompletableFuture<Void>> futures) {
        try {
            /**
             * 等待所有线程执行完成, 这里其实不用强制捕获异常, 这里是为了防止业务异常, 注意:如果提交的任务超出线程池处理的数量,
             * 那么超出的任务将会被线程池执行拒接策略,这样会导致CompletableFuture无法将线程池抛弃的任务标记为已完成,而导致调用
             * allOf方法一直处于阻塞状态
             **/
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        }  catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 等待异步任务执行完成,如果完成则返回结果,否则抛出异常(抛出的是unchecked异常)
     * 与get()方法的区别:不需要强制抛出异常
     * @param future    异步执行业务对象
     */
    public static void blockWaitingUntilFinished(CompletableFuture<Void> future) {
        try {
            // 等待异步线程执行完成, 这里其实不用强制捕获异常, 这里是为了防止业务异常
            future.join();
        }  catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 等待异步任务执行完成,如果完成则返回结果
     * 获取异步执行结果的方式参考: org.hf.application.javabase.jdk8.completableFuture.GetAsyncResultDemo
     * @param future    异步执行业务对象
     */
    public static <T> T blockWaitingUntilFinishedGet(CompletableFuture<T> future) {
        try {
            // 等待异步线程执行完成, 这里需要强制捕获异常
            return future.get();
        }  catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
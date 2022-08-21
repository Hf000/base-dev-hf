package org.hf.application.javabase.jdk8.completableFuture;

import org.hf.application.javabase.utils.ThreadPoolUtil;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * <p> 创建带有返回值的异步任务demo </p>
 * @author hufei
 * @date 2022/8/21 14:15
*/
public class SupplyAsyncDemo {

    public static void main(String[] args) {
        // 获取线程池
        ExecutorService executorService = ThreadPoolUtil.getJdkThreadPool();
        // 创建带有返回值的异步任务
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            //子任务
            try {
                TimeUnit.SECONDS.sleep(3);
                System.out.println(Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 123;
        }, executorService);
        // 当前线程休眠时间
        ThreadPoolUtil.threadSleep(2);
        //主任务
        System.out.println("main Thread");
        //获取子任务结果
        try {
            Integer value = future.get();
            System.out.println("获取异步任务的结果" + value);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        // 关闭线程池
        ThreadPoolUtil.closeJdkThreadPool(executorService);
    }
}

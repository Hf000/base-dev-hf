package org.hf.application.javabase.jdk8.completableFuture;

import org.hf.application.javabase.utils.ThreadPoolUtil;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * <p> 创建没有返回值的异步任务demo </p>
 * @author hufei
 * @date 2022/8/21 14:10
*/
public class RunAsyncDemo {

    public static void main(String[] args) {
        // 获取线程池
        ExecutorService executorService = ThreadPoolUtil.getJdkThreadPool();
        // 创建没有返回值的异步任务
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            //子任务的业务
            try {
                TimeUnit.SECONDS.sleep(3);
                System.out.println(Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, executorService);
        //主任务
        System.out.println("main Thread");
        try {
            System.out.println("获取异步任务结果:" + future.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        ThreadPoolUtil.closeJdkThreadPool(executorService);
    }
}

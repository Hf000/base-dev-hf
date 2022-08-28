package org.hf.application.javabase.jdk8.completableFuture;

import org.hf.application.javabase.utils.ThreadPoolUtil;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * <p> 异步任务执行完成后的回调方法demo,无入参,无返回值 </p >
 * thenRun和thenRunAsync区别
 * 1.thenRun方法是和被调异步任务共用同一个线程,thenRunAsync是新起一个线程
 * 2.thenRunAsync可以指定线程池入参,不指定则采用ForkJoinPool默认线程池
 * @author HUFEI
 * @date 2022-08-23
 **/
public class ThenRunDemo {

    public static void main(String[] args) {
        // 获取线程池
        ExecutorService executorService = ThreadPoolUtil.getJdkThreadPool();
        // 创建异步任务
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            //子任务
            try {
                TimeUnit.SECONDS.sleep(3);
                System.out.println(Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 123;
        }, executorService)
        // 异步任务的回调方法,此方法无入参,无返回值
        .thenRun(() -> {
            // 和被调异步任务共用一个线程
            System.out.println(Thread.currentThread().getName());
            System.out.println("thenRun()回调处理逻辑");
            // 关闭连接池
            ThreadPoolUtil.closeJdkThreadPool(executorService);
        });
        // 异步任务的回调方法,可以指定线程池入参(如果不指定默认采用ForkJoinPool线程池),此方法无入参,无返回值
        /*.thenRunAsync(() -> {
            // 不是和被调异步任务共用一个线程,而是新起一个线程;如果入参不指定线程池,默认采用ForkJoinPool线程池
            System.out.println(Thread.currentThread().getName());
            System.out.println("thenRunAsync()回调方法处理逻辑");
            // 关闭线程池
            ThreadPoolUtil.closeJdkThreadPool(executorService);
        }, executorService);*/
        //主任务
        System.out.println("main Thread");
        //获取子任务结果
        try {
            System.out.println("获取异步任务结果:" + future.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}

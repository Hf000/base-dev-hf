package org.hf.application.javabase.jdk8.completableFuture;

import org.hf.application.javabase.utils.ThreadPoolUtil;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * <p> 异步任务执行完成后执行的回调方法demo,无返回值 </p >
 * thenAccept和thenAcceptAsync区别
 * 1.thenAccept方法是和被调异步任务共用同一个线程,thenAcceptAsync是新起一个线程
 * 2.thenAcceptAsync可以指定线程池入参,不指定则采用ForkJoinPool默认线程池
 * @author HUFEI
 * @date 2022-08-23
 **/
public class ThenAcceptDemo {

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
        // 异步任务的回调方法,若被调异步任务有返回值则入参为被调返回结果,否则入参可以不传,此方法无返回值
        /*.thenAccept(result -> {
            // 和被调异步任务共用一个线程
            System.out.println(Thread.currentThread().getName());
            System.out.println("背调异步任务的结果:" + result);
            System.out.println("thenAccept()回调处理逻辑, 无返回值");
            // 关闭连接池
            ThreadPoolUtil.closeJdkThreadPool(executorService);
        });*/
        // 异步任务的回调方法,若被调异步任务有返回值则入参为被调异步任务的结果,否则入参没有,可以指定线程池入参(如果不指定默认采用ForkJoinPool线程池),此方法无返回值
        .thenAcceptAsync(result -> {
            // 不是和被调异步任务共用一个线程,而是新起一个线程;如果入参不指定线程池,默认采用ForkJoinPool线程池
            System.out.println(Thread.currentThread().getName());
            System.out.println("背调异步任务的结果:" + result);
            System.out.println("thenAcceptAsync()回调处理逻辑, 无返回值");
            // 关闭连接池
            ThreadPoolUtil.closeJdkThreadPool(executorService);
        }, executorService);
        //主任务
        System.out.println("main Thread");
        //获取子任务结果
        try {
            System.out.println("获取任务结果:" + future.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}

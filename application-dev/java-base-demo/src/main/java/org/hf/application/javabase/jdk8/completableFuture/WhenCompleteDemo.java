package org.hf.application.javabase.jdk8.completableFuture;

import org.hf.application.javabase.utils.ThreadPoolUtil;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * <p> 异步任务执行完成后回调方法demo,入参被调任务返回结果或抛出的异常, 有返回值(返回结果是被调方法的结果) </p >
 * 一、whenComplete和whenCompleteAsync区别
 * 1.whenComplete方法是和被调异步任务共用同一个线程,whenCompleteAsync是新起一个线程
 * 2.whenCompleteAsync可以指定线程池入参,不指定则采用ForkJoinPool默认线程池
 * 二、exceptionally方法: 异步任务的回调方法,入参是被调任务抛出的异常,有返回值
 * @author HUFEI
 * @date 2022-08-23
 **/
public class WhenCompleteDemo {

    public static void main(String[] args) {
        // 获取线程池
        ExecutorService executorService = ThreadPoolUtil.getJdkThreadPool();
        // 创建异步任务
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            //子任务
            try {
                int i =1/0;
                TimeUnit.SECONDS.sleep(3);
                System.out.println(Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 123;
        }, executorService);
        // 异步任务的回调方法,入参是被调任务的返回值和抛出的异常,此方法返回值是被调方法的返回结果,如果被调任务抛出了异常,则get()方法获取结果时会抛出异常
        /*CompletableFuture<Integer> future1 = future.whenComplete((result, throwable) -> {
            // 和被调异步任务共用一个线程
            System.out.println(Thread.currentThread().getName());
            // 如果被调任务抛出异常,则结果为null,异常为被调任务抛出的异常,这里的业务处理不受影响
            System.out.println("被调任务的结果:" + result + ", 异常:" + throwable);
            System.out.println("whenComplete()回调处理");
        });*/
        // 异步任务的回调方法,入参是被调任务的返回值和抛出的异常,可以指定线程池入参(如果不指定默认采用ForkJoinPool线程池),此方法返回值是被调方法的返回结果,如果被调任务抛出了异常,则get()方法获取结果时会抛出异常
        CompletableFuture<Integer> future2 = future.whenCompleteAsync((result, throwable) -> {
            // 不是和被调异步任务共用一个线程,而是新起一个线程;如果入参不指定线程池,默认采用ForkJoinPool线程池
            System.out.println(Thread.currentThread().getName());
            // 如果被调任务抛出异常,则结果为null,异常为被调任务抛出的异常,这里的业务处理不受影响
            System.out.println("被调任务的结果:" + result + ", 异常:" + throwable);
            System.out.println("whenCompleteAsync()回调业务处理");
        }, executorService);
        //主任务
        System.out.println("main end ");
        // 异步任务的回调方法,入参是被调任务抛出的异常,有返回值
        CompletableFuture<Integer> future3 = future.exceptionally(throwable -> {
            // 当前主线程执行
            System.out.println(Thread.currentThread().getName());
            System.out.println("被调任务抛出的异常" + throwable);
            return 456;
        });
        //主任务
        System.out.println("main Thread");
        try {
            System.out.println("获取异步任务的结果" + future3.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        // 关闭连接池
        ThreadPoolUtil.closeJdkThreadPool(executorService);
    }
}

package org.hf.application.javabase.jdk8.completableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThenCombineDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(100);

        //异步任务1
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {

            System.out.println("future1:" + Thread.currentThread().getName());
            return "hello";
        }, executorService);

        //异步任务2
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {

            System.out.println("future2:" + Thread.currentThread().getName());
            return "hufei";
        }, executorService);

        //当多个异步任务【全部】执行完毕后，触发后续的任务处理
        CompletableFuture<String> future = future1.thenCombineAsync(future2, (f1, f2) -> {

            System.out.println(Thread.currentThread().getName());
            return f1 + " " + f2;
        },executorService);

        System.out.println(future.get());

        executorService.shutdown();
    }
}

package org.hf.application.javabase.apply.jdk8.completableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ApplyToEitherDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(100);

        CompletableFuture future1 = CompletableFuture.supplyAsync(()->{
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("future1："+Thread.currentThread().getName());
            return "hello";
        },executorService);


        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(()-> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("future2："+Thread.currentThread().getName());
            return "hufei";
        },executorService);

        CompletableFuture future = future1.applyToEitherAsync(future2, (value) -> {
            System.out.println("result" + Thread.currentThread().getName());
            return value;
        }, executorService);

        System.out.println(future.get());

        executorService.shutdown();


    }
}

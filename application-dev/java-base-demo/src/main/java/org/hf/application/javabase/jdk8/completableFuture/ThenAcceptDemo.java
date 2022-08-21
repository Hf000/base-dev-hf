package org.hf.application.javabase.jdk8.completableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThenAcceptDemo {

    public static void main(String[] args) {

        ExecutorService executorService = Executors.newFixedThreadPool(100);

        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {

            //子任务
            try {
                TimeUnit.SECONDS.sleep(3);
                System.out.println(Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return 123;
        }, executorService).thenAcceptAsync(value->{
            System.out.println("被连接的任务执行了，前置任务的结果为："+value);
            executorService.shutdown();
        },executorService);

        //主任务
        System.out.println("main end ");

        //获取子任务结果
        /*try {
            Integer value = future.get();
            System.out.println(value);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }*/



    }
}

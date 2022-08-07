package org.hf.application.javabase.apply.jdk8.completableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class RunAsyncDemo {

    public static void main(String[] args) {

        //指定线程池
        ExecutorService executorService = Executors.newFixedThreadPool(100);

        CompletableFuture.runAsync(()->{
            //子任务的业务
            try {
                TimeUnit.SECONDS.sleep(3);
                System.out.println(Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },executorService);

        //主任务
        System.out.println("main end");

        executorService.shutdown();
    }
}

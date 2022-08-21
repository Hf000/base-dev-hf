package org.hf.application.javabase.jdk8.completableFuture;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThenAcceptBothDemo {

    public static void main(String[] args) {

        ExecutorService executorService = Executors.newFixedThreadPool(100);

        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            int f1 = new Random().nextInt(100);
            System.out.println("f1 value：" + f1);
            return f1;
        },executorService);

        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> {
            int f2 = new Random().nextInt(100);
            System.out.println("f2 value：" + f2);
            return f2;
        },executorService);

        //多个异步任务【全部】执行完毕，ThenAcceptBoth-》没有返回值的
        future1.thenAcceptBothAsync(future2,(f1,f2)-> System.out.println(f1+f2),executorService);

        executorService.shutdown();
    }
}

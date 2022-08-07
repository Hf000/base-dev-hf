package org.hf.application.javabase.apply.jdk8.completableFuture;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AnyOfDemo {

    public static void main(String[] args) {

        ExecutorService executorService = Executors.newFixedThreadPool(100);

        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            int f1 = new Random().nextInt(100);
            System.out.println("f1 value：" + f1);
            return f1;
        },executorService);


        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int f2 = new Random().nextInt(100);
            System.out.println("f2 value：" + f2);
            return f2;
        },executorService);


        CompletableFuture<Integer> future3 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int f3 = new Random().nextInt(100);
            System.out.println("f3 value：" + f3);
            return f3;
        },executorService);


        List<CompletableFuture<Integer>> list = new ArrayList<>();
        list.add(future1);
        list.add(future2);
        list.add(future3);

        CompletableFuture<Object> future = CompletableFuture.anyOf(list.toArray(new CompletableFuture[]{}));

        future.thenRunAsync(()->{

            try {
                System.out.println("最快的线程任务结果是："+future.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
    }
}

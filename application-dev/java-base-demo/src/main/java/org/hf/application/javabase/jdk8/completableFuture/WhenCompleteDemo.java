package org.hf.application.javabase.jdk8.completableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class WhenCompleteDemo {

    public static void main(String[] args) {

        ExecutorService executorService = Executors.newFixedThreadPool(100);

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

        //主任务
        System.out.println("main end ");

        //根据异步任务的结果，触发后续业务逻辑处理
        /*future.whenCompleteAsync(new BiConsumer<Integer, Throwable>() {
            @Override
            public void accept(Integer integer, Throwable throwable) {

                System.out.println("异步任务后续处理线程："+Thread.currentThread().getName());
                System.out.println(integer+123);
                executorService.shutdown();
            }
        },executorService);*/

        /*future.exceptionally(new Function<Throwable, Integer>() {
            @Override
            public Integer apply(Throwable throwable) {

                System.out.println("异常结果处理的线程："+Thread.currentThread().getName());
                System.out.println(throwable.getMessage());
                return null;
            }
        });*/

        future.exceptionally((t)->{
            System.out.println("异常结果处理的线程："+Thread.currentThread().getName());
            return null;
        });

    }
}

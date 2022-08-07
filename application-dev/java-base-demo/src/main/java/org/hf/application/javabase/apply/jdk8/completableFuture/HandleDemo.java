package org.hf.application.javabase.apply.jdk8.completableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class HandleDemo {

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
        }, executorService).handleAsync((value,throwable)->{

            int result  =1;
            if (throwable == null){
                //代表前置任务执行没有出现异常
                result = value*10;
                System.out.println(result);
            }else {
                //前置任务执行出现异常
                System.out.println(throwable.getMessage());
            }

            return result;
        },executorService);

        //主任务
        System.out.println("main end ");

        //获取子任务结果
        try {
            Integer value = future.get();
            System.out.println(value);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        executorService.shutdown();

    }
}

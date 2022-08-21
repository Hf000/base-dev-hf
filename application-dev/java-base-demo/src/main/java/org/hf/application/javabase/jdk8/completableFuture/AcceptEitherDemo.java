package org.hf.application.javabase.apply.jdk8.completableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * <p> 获取先执行完的线程的结果 </p>
 * @author hufei
 * @date 2022/8/13 20:03
*/
public class AcceptEitherDemo {

    public static void main(String[] args) {

        // 创建一个线程池
        ExecutorService executorService = Executors.newFixedThreadPool(100);

        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(()->{
            try {
                TimeUnit.SECONDS.sleep(2);
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

        // 哪个线程先执行完成就获取哪个线程的结果
        future1.acceptEitherAsync(future2,(value)->{

            System.out.println(value);
            executorService.shutdown();
        },executorService);
    }
}

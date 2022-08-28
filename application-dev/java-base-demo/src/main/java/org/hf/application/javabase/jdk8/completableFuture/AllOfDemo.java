package org.hf.application.javabase.jdk8.completableFuture;

import org.hf.application.javabase.utils.ThreadPoolUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p> 所有任务都正常执行完成后才会执行,无返回值 </p >
 * allOf和anyOf
 * 一、相同点:
 * 1.都可以组合多个任务
 * 二、不同点:
 * 1.allOf需要所有的任务正常执行完成才会执行,anyOf执行要一个任务执行完成就会执行
 * 2.allOf如果有任务抛出异常,则获取结果时抛出异常,anyOf如果最快执行的任务抛出异常则不会执行后续逻辑
 * @author hufei
 * @date 2022/8/28 15:48
*/
public class AllOfDemo {

    public static void main(String[] args) {
        // 获取线程池
        ExecutorService executorService = ThreadPoolUtil.getJdkThreadPool();
        // 创建异步任务
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            int f1 = new Random().nextInt(100);
            System.out.println("future1：" + f1);
            return f1;
        }, executorService);
        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int f2 = new Random().nextInt(100);
            System.out.println("future2：" + f2);
            return f2;
        }, executorService);
        CompletableFuture<Integer> future3 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int f3 = new Random().nextInt(100);
//            int i = 1/0;
            System.out.println("future3：" + f3);
            return f3;
        }, executorService);
        //后续任务回调处理
        List<CompletableFuture<Integer>> list = new ArrayList<>();
        list.add(future1);
        list.add(future2);
        list.add(future3);
        // 所有任务都正常执行完成后才会执行,无返回值, 如果有任务执行异常获取结果时会抛出异常
        CompletableFuture<Void> all = CompletableFuture.allOf(list.toArray(new CompletableFuture[]{}));
        try {
            System.out.println("获取结果:" + all.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        // 所有任务执行完成后的处理逻辑
        list.forEach(future -> {
            try {
                System.out.println("所有任务结果:" + future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
        // 异步任务执行完成后的回调方法,无入参,无返回值
        all.thenRunAsync(() -> {
            AtomicInteger atomicInteger = new AtomicInteger();
            list.forEach(future -> {
                try {
                    Integer value = future.get();
                    atomicInteger.updateAndGet(v -> v + value);
                    System.out.println(atomicInteger);
                    // 关闭线程池
                    ThreadPoolUtil.closeJdkThreadPool(executorService);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            });
        }, executorService);
        // 关闭线程池
//        ThreadPoolUtil.closeJdkThreadPool(executorService);
    }
}

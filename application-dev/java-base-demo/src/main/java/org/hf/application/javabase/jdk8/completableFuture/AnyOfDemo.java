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

/**
 * <p> 所有任务中只要有一个任务正常执行完成后就会执行,有返回值 </p >
 * allOf和anyOf
 * 一、相同点:
 * 1.都可以组合多个任务
 * 二、不同点:
 * 1.allOf需要所有的任务正常执行完成才会执行,anyOf执行要一个任务执行完成就会执行
 * 2.allOf如果有任务抛出异常,则获取结果时抛出异常,anyOf如果最快执行的任务抛出异常则不会执行后续逻辑
 * @author hufei
 * @date 2022/8/28 15:48
 */
public class AnyOfDemo {

    public static void main(String[] args) {
        // 获取线程池
        ExecutorService executorService = ThreadPoolUtil.getJdkThreadPool();
        // 创建异步任务
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            int f1 = new Random().nextInt(100);
            System.out.println("future1：" + f1);
//            int i = 1/0;
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
            System.out.println("future3：" + f3);
            return f3;
        }, executorService);
        // 将任务对象加入集合中
        List<CompletableFuture<Integer>> list = new ArrayList<>();
        list.add(future1);
        list.add(future2);
        list.add(future3);
        // 所有任务中只要有一个任务正常执行完成后就会执行,有返回值, 如果最先执行完成的线程抛出了异常则不会执行
        CompletableFuture<Object> future = CompletableFuture.anyOf(list.toArray(new CompletableFuture[]{}));
        // 应用
        future.thenRunAsync(() -> {
            try {
                System.out.println("最快的线程任务结果是："+future.get());
                ThreadPoolUtil.closeJdkThreadPool(executorService);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
        // 关闭线程池
//        ThreadPoolUtil.closeJdkThreadPool(executorService);
    }
}

package org.hf.application.javabase.jdk8.completableFuture;

import org.hf.application.javabase.utils.ThreadPoolUtil;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * <p> 多个异步任务处理完成后执行的回调方法demo,没有返回值,也没有入参 </p >
 * thenCombineAsync、thenAcceptBothAsync和runAfterBothAsync方法
 * 一、相同点:
 *  1.都是将两个任务组合起来处理;
 *  2.如果两个任务都正常执行就会进行后续的逻辑处理;
 *  3.如果有任务抛了异常则不会执行.
 * 二、不同点:
 * thenCombineAsync方法有返回值,入参1:异步任务对象,入参2:调用的异步任务结果和传入的异步任务结果
 * thenAcceptBothAsync方法无返回值,入参1:异步任务对象,入参2:调用的异步任务结果和传入的异步任务结果
 * runAfterBothAsync方法没有返回值,入参:异步任务对象
 * @author HUFEI
 * @date 2022-08-23
 **/
public class RunAfterBothDemo {

    public static void main(String[] args) {
        // 获取线程池
        ExecutorService executorService = ThreadPoolUtil.getJdkThreadPool();
        // 创建异步任务
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            int f1 = new Random().nextInt(100);
            System.out.println("f1 value：" + f1);
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
            System.out.println("f2 value：" + f2);
            return f2;
        }, executorService);
        // 多个异步任务【全部】执行完毕，ThenAcceptBoth-》没有返回值的, 入参:异步任务对象, 如果有任务抛了异常则不会执行
        future1.runAfterBothAsync(future2, () -> {
            System.out.println("两个任务都已执行完");
            // 关闭线程池
            ThreadPoolUtil.closeJdkThreadPool(executorService);
        }, executorService);
        // 关闭线程池
//        ThreadPoolUtil.closeJdkThreadPool(executorService);
    }
}

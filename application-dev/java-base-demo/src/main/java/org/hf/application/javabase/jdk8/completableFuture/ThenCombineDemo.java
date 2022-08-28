package org.hf.application.javabase.jdk8.completableFuture;

import org.hf.application.javabase.utils.ThreadPoolUtil;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p> 多个异步任务处理完成后执行的回调方法demo,有返回值 </p >
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
public class ThenCombineDemo {

    public static void main(String[] args) {
        // 获取线程池
        ExecutorService executorService = ThreadPoolUtil.getJdkThreadPool();
        // 创建一个带有返回值的异步任务
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("future1:" + Thread.currentThread().getName());
            return "future1";
        }, executorService);
        //异步任务2
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("future2:" + Thread.currentThread().getName());
//            int i = 1/0;
            return "future2";
        }, executorService);
        // 当多个异步任务【全部】执行完毕后，触发后续的任务处理, 入参1:异步任务对象, 入参2:调用的异步任务结果和传入的异步任务结果, 如果有任务抛了异常则不会执行
        CompletableFuture<String> future = future1.thenCombineAsync(future2, (f1, f2) -> {
            System.out.println("future:" + Thread.currentThread().getName());
            return f1 + " - " + f2;
        }, executorService);
        try {
            System.out.println("获取异步任务结果:" + future.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        // 关闭线程池
        ThreadPoolUtil.closeJdkThreadPool(executorService);
    }
}

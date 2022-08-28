package org.hf.application.javabase.jdk8.completableFuture;

import org.hf.application.javabase.utils.ThreadPoolUtil;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * <p> 只要其中一个任务先正常执行完成,则执行后续逻辑demo </p >
 * applyToEitherAsync、acceptEitherAsync和runAfterEitherAsync方法
 * 一、相同点:
 *  1.都是将两个任务组合起来处理;
 *  2.先执行完成的任务正常完成,后执行完的任务抛出异常也不影响后续的逻辑处理;
 * 二、不同点:
 * applyToEitherAsync方法有返回值,入参1:异步任务对象,入参2:获取先执行完的异步任务结果
 * acceptEitherAsync方法无返回值,入参1:异步任务对象,入参2:获取先执行完的异步任务结果
 * runAfterEitherAsync方法没有返回值,入参:异步任务对象
 * @author HUFEI
 * @date 2022-08-23
 **/
public class RunAfterEitherDemo {

    public static void main(String[] args) {
        // 获取线程池
        ExecutorService executorService = ThreadPoolUtil.getJdkThreadPool();
        // 创建异步任务
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(()->{
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            int i = 1/0;
            System.out.println("future1："+Thread.currentThread().getName());
            return "future1";
        }, executorService);
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(()-> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("future2："+Thread.currentThread().getName());
            return "future2";
        }, executorService);
        // 当其中一个任务正常执行完成时,就会执行后续逻辑,入参:异步任务对象,无返回值,后执行完成的任务抛异常也不影响后续逻辑执行
        future1.runAfterEitherAsync(future2, () -> {
            System.out.println("result");
            // 关闭线程池
            ThreadPoolUtil.closeJdkThreadPool(executorService);
        }, executorService);
    }
}

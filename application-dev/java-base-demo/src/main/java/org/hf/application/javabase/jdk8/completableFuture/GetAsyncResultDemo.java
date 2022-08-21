package org.hf.application.javabase.jdk8.completableFuture;

import org.hf.application.javabase.utils.ThreadPoolUtil;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * <p> 获取异步任务结果的几种方式demo </p>
 * @author hufei
 * @date 2022/8/21 14:26
*/
public class GetAsyncResultDemo {

    public static void main(String[] args) {
        // 获取线程池
        ExecutorService executorService = ThreadPoolUtil.getJdkThreadPool();
        // 创建一个带有返回值的异步任务
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            //子任务
            try {
                TimeUnit.SECONDS.sleep(3);
                System.out.println(Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "结果";
        }, executorService);
        //主任务
        System.out.println("main Thread");
        //获取子任务结果
        try {
            // 1.等待任务执行完成,如果完成则返回结果,否则抛出异常;与join()方法的区别:需要强制抛出异常
            System.out.println("get()方法获取异步任务结果:" + future.get());
            // 3.在给定的超时时间内等待异步任务执行完成,完成则返回结果,否则抛出异常;入参:超时时间和时间单位
//            System.out.println("get(long timeout, TimeUnit unit)方法获取异步任务结果:" + future.get(2, TimeUnit.SECONDS));
            // 4.立即获取异步任务的结果,如果完成则获取结果或抛出遇到的异常,若未执行完成则返回给定的默认值
            /*TimeUnit.SECONDS.sleep(2);
            System.out.println("getNow()方法获取异步任务结果:" + future.getNow("默认结果"));*/
            // 5.如果异步任务没有执行完成则返回true,且get()获取的值是给定的值;如果异步任务执行完成则返回false,且get()获取到的值是被调异步任务的结果. complete()需要结合get()等方法使用
            /*ThreadPoolUtil.threadSleep(2);
            boolean flag = future.complete("默认结果");
            if (flag) {
                System.out.println("complete()方法执行后,异步任务未执行完成,且获取的异步任务结果是complete方法设置的默认值:"+ future.get());
            } else {
                System.out.println("complete()方法执行后,异步任务执行完成,且获取的异步任务结果是被调异步任务的返回值:"+ future.get());
            }*/
            // 6.如果异步任务没有执行完成则返回true,且get()获取值时会抛出指定的异常;如果异步任务执行完成则返回false,且get()获取到的值是被调异步任务的结果. completeExceptionally()需要结合get()等方法使用
            /*ThreadPoolUtil.threadSleep(2);
            boolean flag = future.completeExceptionally(new RuntimeException());
            if (flag) {
                System.out.println("completeExceptionally()方法执行后,异步任务未执行完成,且获取值时抛出completeExceptionally方法指定的异常:"+ future.get());
            } else {
                System.out.println("completeExceptionally()方法执行后,异步任务执行完成,且获取的异步任务结果是被调异步任务的返回值:"+ future.get());
            }*/
        } catch (InterruptedException | ExecutionException /*| TimeoutException*/ e) {
            e.printStackTrace();
        }
        // 2.等待异步任务执行完成,如果完成则返回结果,否则抛出异常(抛出的是unchecked异常);与get()方法的区别:不需要强制抛出异常
//        System.out.println("join()方法获取异步任务结果:" + future.join());
        // 关闭线程池
        ThreadPoolUtil.closeJdkThreadPool(executorService);
    }
}

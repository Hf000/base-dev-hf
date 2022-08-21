package org.hf.application.javabase.jdk8.completableFuture;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.hf.application.javabase.utils.ThreadPoolUtil;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <p> 获取先执行完的线程的结果demo </p>
 * @author hufei
 * @date 2022/8/13 20:03
*/
public class AcceptEitherDemo {

    public static void main(String[] args) {
        // 获取线程池
        ExecutorService executorService = ThreadPoolUtil.getJdkThreadPool();
        // 创建异步任务
        CompletableFuture<String> future = CompletableFuture.supplyAsync(()->{
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("future："+Thread.currentThread().getName());
            return "hello one";
        }, executorService);
        CompletableFuture<String> futureTwo = CompletableFuture.supplyAsync(()-> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("futureTwo："+Thread.currentThread().getName());
            return "hello two";
        }, executorService);
        // 哪个线程先执行完成就获取哪个线程的结果
        future.acceptEitherAsync(futureTwo, (value) -> {
            System.out.println("获取到的结果" + value);
            ThreadPoolUtil.closeJdkThreadPool(executorService);
        }, executorService);
    }
}

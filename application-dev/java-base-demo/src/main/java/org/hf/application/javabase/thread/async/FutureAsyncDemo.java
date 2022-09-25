package org.hf.application.javabase.thread.async;

import org.hf.application.javabase.utils.ThreadPoolUtil;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * <p> 采用Future进行API的异步调用 </p>
 *
 * @author hufei
 * @date 2022/9/3 16:55
 */
public class FutureAsyncDemo {

    /**
     * 获取线程池
     */
    static ExecutorService executorService = ThreadPoolUtil.getJdkThreadPool();

    /**
     * 计算文章得分
     *
     * @param aName 入参
     * @return int
     */
    public static int getArticleScore(String aName) {
        //开启一个异步任务
        Future<Integer> futureA = executorService.submit(new CalculateArticleScoreA());
        Future<Integer> futureB = executorService.submit(new CalculateArticleScoreB());
        Future<Integer> futureC = executorService.submit(new CalculateArticleScoreC());
        doSomeThings();
        //获取异步任务的执行结果
        Integer a = 0;
        try {
            a = futureA.get();
        } catch (InterruptedException | ExecutionException e) {
            futureA.cancel(true);
            e.printStackTrace();
        }
        Integer b = 0;
        try {
            b = futureB.get(3L, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            futureB.cancel(true);
            e.printStackTrace();
        }
        Integer c = 0;
        try {
            c = futureC.get();
        } catch (InterruptedException | ExecutionException e) {
            futureC.cancel(true);
            e.printStackTrace();
        }
        // 关闭线程池
        ThreadPoolUtil.closeJdkThreadPool(executorService);
        return a + b + c;
    }

    private static void doSomeThings() {
        System.out.println(" main do some things");
    }

    private static class CalculateArticleScoreA implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            //业务操作
            Random random = new Random();
            TimeUnit.SECONDS.sleep(10);
            System.out.println(Thread.currentThread().getName());
            return random.nextInt(100);
        }
    }

    private static class CalculateArticleScoreB implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            //业务操作
            Random random = new Random();
            TimeUnit.SECONDS.sleep(20);
            System.out.println(Thread.currentThread().getName());
            return random.nextInt(100);
        }
    }

    private static class CalculateArticleScoreC implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            //业务操作
            Random random = new Random();
            TimeUnit.SECONDS.sleep(30);
            System.out.println(Thread.currentThread().getName());
            return random.nextInt(100);
        }
    }

    public static void main(String[] args) {
        System.out.println(getArticleScore("demo"));
    }
}

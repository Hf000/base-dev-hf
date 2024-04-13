package org.hf.application.javabase.thread.async;

import java.util.Random;

/**
 * <p> API同步调用 </p>
 * 采用异步执行的方式优化同步调用的几种方法:
 * 1. 采用线程池提交返回Future接收异步结果: org.hf.application.javabase.thread.async.FutureAsyncDemo
 * 2. 采用ExecutorCompletionService线程池完成服务获取异步结果: org.hf.application.javabase.thread.async.CompletionServiceDemo
 * 3. jdk8及以上版本采用completableFuture方式: org.hf.application.javabase.jdk8.completableFuture.AllOfDemo
 * 4. 采用CountDownLatch方式进行所有异步结果获取: org.hf.application.javabase.thread.async.CountDownLatchDemo
 * @author hufei
 * @date 2022/9/3 18:20
 */
public class SyncDemo {

    static Random random = new Random();

    /**
     * 接收文章名称，计算出文章的分数
     *
     * @param aName 入参
     * @return int
     */
    public static int getArticleScore(String aName) {
        int a = calculateArticleScore(aName);
        int b = calculateArticleScore(aName);
        int c = calculateArticleScore(aName);
        //其他业务处理
        doSomeThings();
        //返回文章分数
        return a + b + c;
    }

    private static void doSomeThings() {
        System.out.println("do some things");
    }

    //计算文章分数
    private static int calculateArticleScore(String aName) {
        //模拟延迟
        otherService();
        return random.nextInt(100);
    }

    private static void otherService() {
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
    }

    public static void main(String[] args) {
        System.out.println(getArticleScore("demo"));
    }
}

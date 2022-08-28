package org.hf.application.javabase.jdk8.async;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FutureAsyncDemo {

    static ExecutorService executorService = Executors.newFixedThreadPool(100);

    //计算文章得分
    public static int getArticleScore(String aName){

        //开启一个异步任务
        Future<Integer> futureA = executorService.submit(new CalculateArticleScoreA());
        Future<Integer> futureB = executorService.submit(new CalculateArticleScoreB());
        Future<Integer> futureC = executorService.submit(new CalculateArticleScoreC());

        doSomeThings();

        //获取异步任务的执行结果
        Integer a = null;
        try {
            a = futureA.get();
        } catch (InterruptedException e) {
            futureA.cancel(true);
            e.printStackTrace();
        } catch (ExecutionException e) {
            futureA.cancel(true);
            e.printStackTrace();
        }

        Integer b = null;
        try {
            b = futureB.get(3L,TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            futureB.cancel(true);
            e.printStackTrace();
        } catch (ExecutionException e) {
            futureB.cancel(true);
            e.printStackTrace();
        } catch (TimeoutException e) {
            futureB.cancel(true);
            e.printStackTrace();
        }

        Integer c = null;
        try {
            c = futureC.get();
        } catch (InterruptedException e) {
            futureC.cancel(true);
            e.printStackTrace();
        } catch (ExecutionException e) {
            futureC.cancel(true);
            e.printStackTrace();
        }

        executorService.shutdown();

        return a+b+c;

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

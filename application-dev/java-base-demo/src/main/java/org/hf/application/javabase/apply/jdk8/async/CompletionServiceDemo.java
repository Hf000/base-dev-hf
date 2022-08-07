package org.hf.application.javabase.apply.jdk8.async;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CompletionServiceDemo {

    static ExecutorService executorService = Executors.newFixedThreadPool(100);

    public static void main(String[] args) {

        ExecutorCompletionService<Integer> completionService = new ExecutorCompletionService<>(executorService);

        completionService.submit(new CalculateArticleScoreA());
        completionService.submit(new CalculateArticleScoreB());
        completionService.submit(new CalculateArticleScoreC());

        doSomeThings();

        Integer result = 0;

        for (int i = 0; i < 3; i++) {

            try {
                result+=completionService.take().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        System.out.println(result);

        executorService.shutdown();
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

            TimeUnit.SECONDS.sleep(30);
            System.out.println(Thread.currentThread().getName());
            return random.nextInt(100);
        }
    }
    private static class CalculateArticleScoreC implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            //业务操作
            Random random = new Random();

            TimeUnit.SECONDS.sleep(20);
            System.out.println(Thread.currentThread().getName());
            return random.nextInt(100);
        }
    }
}

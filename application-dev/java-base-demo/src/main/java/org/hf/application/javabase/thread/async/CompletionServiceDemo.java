package org.hf.application.javabase.thread.async;

import org.hf.application.javabase.utils.ThreadPoolUtil;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * <p> 采用线程池完成服务进行API异步调用 </p>
 *
 * @author hufei
 * @date 2022/8/28 17:39
 */
public class CompletionServiceDemo {

    /**
     * 获取线程池
     */
    static ExecutorService executorService = ThreadPoolUtil.getJdkThreadPool();

    public static void main(String[] args) {
        // 创建线程池完成服务对象
        ExecutorCompletionService<Integer> completionService = new ExecutorCompletionService<>(executorService);
        // 提交多个线程任务
        completionService.submit(new CalculateArticleScoreA());
        completionService.submit(new CalculateArticleScoreB());
        completionService.submit(new CalculateArticleScoreC());
        // 其他业务
        doSomeThings();
        Integer result = 0;
        for (int i = 0; i < 3; i++) {
            try {
                // 获取任务结果
                result += completionService.take().get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        System.out.println("最终结果:" + result);
        // 关闭线程池
        ThreadPoolUtil.closeJdkThreadPool(executorService);
    }

    private static void doSomeThings() {
        System.out.println(" main do some things");
    }

    /**
     * 通过实现Callable接口的方式创建子线程任务
     */
    private static class CalculateArticleScoreA implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            //业务操作
            Random random = new Random();
            TimeUnit.SECONDS.sleep(10);
            System.out.println(Thread.currentThread().getName());
            int value = random.nextInt(100);
            System.out.println("返回结果:" + value);
            return value;
        }
    }

    private static class CalculateArticleScoreB implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            //业务操作
            Random random = new Random();
            TimeUnit.SECONDS.sleep(30);
            System.out.println(Thread.currentThread().getName());
            int value = random.nextInt(100);
            System.out.println("返回结果:" + value);
            return value;
        }
    }

    private static class CalculateArticleScoreC implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            //业务操作
            Random random = new Random();
            TimeUnit.SECONDS.sleep(20);
            System.out.println(Thread.currentThread().getName());
            int value = random.nextInt(100);
            System.out.println("返回结果:" + value);
            return value;
        }
    }
}

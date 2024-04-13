package org.hf.application.javabase.thread.async;

import org.hf.application.javabase.utils.CountdownLatchUtil;
import org.hf.application.javabase.utils.ThreadPoolUtil;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * <p> 采用CountDownLatch计数器方式实现API异步调用 </p >
 *
 * @author HF
 * @date 2024-04-01
 **/
public class CountDownLatchDemo {

    public static void main(String[] args) {
        // CountDownLatch简单应用
//        simpleDemo();
        // 有返回值的简单逻辑分批处理应用
//        submitTaskPartitionDemo();
        // 有返回值简单逻辑应用  推荐此方式
        submitTaskDemo();
        // 无返回值简单应用
//        taskExecuteDemo();
    }

    private static void simpleDemo() throws InterruptedException {
        // 创建CountDownLatch对象,这里初始化时的数量表示控制的线程并发数量
        CountDownLatch latch = new CountDownLatch(3);
        // 启动三个线程执行任务
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                // 执行任务
                System.out.println(Thread.currentThread().getName() + " 执行任务");
                // 调用一次计数器减一
                latch.countDown();
            }).start();
        }
        // 阻塞着，等待所有任务完成, 当计数器为0时,表示所有线程执行完成,这里才能获取锁成功,否则获取锁失败,线程加入同步队列中阻塞,当计数器为0时则会被唤醒
        latch.await();
        // 所有任务完成后，继续执行
        System.out.println("所有任务完成");
    }

    public static void submitTaskPartitionDemo() {
        List<Integer> customList = new ArrayList<>();
        for (int i = 0; i < 47; i++) {
            customList.add(i);
        }
        // 获取线程池
        ThreadPoolTaskExecutor springThreadPool = ThreadPoolUtil.getSpringThreadPool();
        long start = System.currentTimeMillis();
        // 注意: 这种方式虽然减少了提交任务次数, 但是不能充分发挥线程池的作用, 可能导致有些线程闲置, 而有些线程执行了很久
        List<List<Integer>> partitionList = com.google.common.collect.Lists.partition(customList, 10);
        CountDownLatch taskLatch = new CountDownLatch(partitionList.size());
        partitionList.forEach(itemList -> {
            CountdownLatchUtil.asyncTaskHandler(() -> {
                try {
                    // 异步执行逻辑
                    for (Integer item : itemList) {
                        TimeUnit.MILLISECONDS.sleep(item * 20);
                        System.out.println(item);
                    }
                } catch (Exception e) {
                    System.out.println("异步执行逻辑异常");
                } finally {
                    // 注意, 此步骤不能在异步业务执行之前调用, 不然会出现异步业务没执行完就调用了await()方法从而导致异步执行业务中断
                    taskLatch.countDown();
                }
                return null;
            }, springThreadPool);
        });
        try {
            // 计数器不为0时阻塞主线程, 当计数器为0时, 会唤醒主线程
            taskLatch.await();
        } catch (Exception e) {
            System.out.println("等待任务执行完成获取结果报错");
        } finally {
            // 注意关闭线程池前需要保证子线程的业务都执行完, 所以这里将taskLatch.countDown()放在异步执行逻辑之后调用
            ThreadPoolUtil.closeSpringThreadPool(springThreadPool);
        }
        long end = System.currentTimeMillis() - start;
        System.out.println("结束" + end);
    }

    public static void submitTaskDemo() {
        List<Integer> customList = new ArrayList<>();
        for (int i = 0; i < 47; i++) {
            customList.add(i);
        }
        // 获取线程池
        ThreadPoolTaskExecutor springThreadPool = ThreadPoolUtil.getSpringThreadPool();
        long start = System.currentTimeMillis();
//        List<Future<Long>> futureList = new ArrayList<>();
        CountDownLatch taskLatch = new CountDownLatch(customList.size());
        customList.forEach(item -> {
            Future<Long> futureInfo = CountdownLatchUtil.asyncTaskHandler(() -> {
                try {
                    // 异步执行逻辑, 注意, 如果这里进行线程睡眠的话, 需要注意线程池的队列是否会消耗完, 如果消耗完会导致计数器无法归0而阻塞主线程
                    TimeUnit.MILLISECONDS.sleep(item * 20);
                    System.out.println(item);
                } catch (Exception e) {
                    System.out.println("异步执行逻辑异常");
                } finally {
                    // 注意, 此步骤不能在异步业务执行之前调用, 不然会出现异步业务没执行完就调用了await()方法从而导致异步执行业务中断
                    taskLatch.countDown();
                }
                return null;
            }, springThreadPool);
//            futureList.add(futureInfo);
        });
        try {
            // 计数器不为0时阻塞主线程, 当计数器为0时, 会唤醒主线程
            taskLatch.await();
            // 获取执行结果
//        List<Long> resp = new ArrayList<>();
//        futureList.forEach(future -> {
//            resp.add(CountdownLatchUtil.fetchTaskResult(future));
//        });
//        System.out.println("获取返回结果" + resp);
//        System.out.println("获取返回结果" + resp.size());
        } catch (Exception e) {
            System.out.println("等待任务执行完成获取结果报错");
        } finally {
            // 注意关闭线程池前需要保证子线程的业务都执行完, 所以这里将taskLatch.countDown()放在异步执行逻辑之后调用
            ThreadPoolUtil.closeSpringThreadPool(springThreadPool);
        }
        long end = System.currentTimeMillis() - start;
        System.out.println("结束" + end);
    }

    public static void taskExecuteDemo() {
        ThreadPoolTaskExecutor springThreadPool = ThreadPoolUtil.getSpringThreadPool();
        CountDownLatch latch = new CountDownLatch(1);
        CountdownLatchUtil.asyncTaskHandler(() -> {
            ThreadPoolUtil.threadSleep(2);
            System.out.println("测试");
            latch.countDown();
        }, springThreadPool);
        try {
            // 计数器不为0时阻塞主线程, 当计数器为0时, 会唤醒主线程
            latch.await();
        } catch (Exception e) {
            System.out.println("等待任务执行完成获取结果报错");
        } finally {
            // 注意关闭线程池前需要保证子线程的业务都执行完, 所以这里将latch.countDown()放在异步执行逻辑之后调用
            ThreadPoolUtil.closeSpringThreadPool(springThreadPool);
        }
        System.out.println("结束");
    }
}
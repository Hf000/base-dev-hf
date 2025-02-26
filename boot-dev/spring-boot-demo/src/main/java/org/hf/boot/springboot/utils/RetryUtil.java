package org.hf.boot.springboot.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 调用报错重试工具类
 * @author HF
 */
@Slf4j
public class RetryUtil {

    /**
     * 延迟的时间间隔数组, 单位:毫秒
     */
    private static final int[] delayTimeArray = new int[]{1000 * 60, 1000 * 60 * 3};

    /**
     * 执行方法报错重试,执行3次,重试2次,第一次重试在第一次执行失败1分钟后,第二次重试在第一次执行失败3分钟后
     * @param function 执行传入的函数逻辑,获取执行结果
     * @param <R>      返回类型
     * @return 执行结果
     */
    public static <R> R callFunctionRetry(Supplier<R> function) {
        return doRetry(delayTimeArray, function);
    }

    /**
     * 执行线程任务报错重试,执行3次,重试2次,第一次重试在第一次执行失败1分钟后,第二次重试在第一次执行失败3分钟后
     * @param runnable 线程任务
     */
    public static void runThreadTaskRetry(Runnable runnable) {
        doRetry(delayTimeArray, runnable);
    }

    /**
     * 根据指定时间重试
     * @param delayTimeArray 重试时间数组
     * @param runnable 重试任务
     */
    public static void doRetry(int[] delayTimeArray, Runnable runnable) {
        try {
            // 立即执行一次,成功则则直接返回,失败则重试
            runnable.run();
            return;
        } catch (Exception e) {
            log.error("doRetry fail", e);
        }
        DelayQueue<Delayed> delayQueue = new DelayQueue<>();
        for (int delayTime : delayTimeArray) {
            delayQueue.put(new DelayElement(delayTime));
        }
        int size = delayQueue.size();
        while (!delayQueue.isEmpty()) {
            try {
                // 阻塞获取
                delayQueue.take();
                log.info("doRetry currRetryCount={} | dateTime={}", size - delayQueue.size(), DateFormat.getDateTimeInstance().format(new Date()));
                // 执行任务
                runnable.run();
                return;
            } catch (InterruptedException e) {
                log.error("doRetry take fail", e);
            } catch (Exception e) {
                log.error("doRetry fail", e);
            }
        }
    }

    /**
     * 根据指定时间重试
     * @param delayTimeArray 重试时间数组
     * @param function 重试任务
     * @return 执行结果
     * @param <R> 泛型结果
     */
    public static <R> R doRetry(int[] delayTimeArray, Supplier<R> function) {
        try {
            // 立即执行一次,成功则则直接返回,失败则重试
            return function.get();
        } catch (Exception e) {
            log.error("doRetry fail", e);
        }
        // 将重试时间加入延迟队列
        DelayQueue<DelayElement> delayQueue = new DelayQueue<>();
        for (int delayTime : delayTimeArray) {
            delayQueue.put(new DelayElement(delayTime));
        }
        while (!delayQueue.isEmpty()) {
            try {
                // 阻塞获取并执行
                DelayElement take = delayQueue.take();
                log.info("doRetry begin delayTime={} | dateTime={}", take.delayTime , DateFormat.getDateTimeInstance().format(new Date()));
                // 执行任务处理逻辑
                return function.get();
            } catch (InterruptedException e) {
                log.error("doRetry take fail", e);
            } catch (Exception e) {
                log.error("doRetry fail", e);
            }
        }
        return null;
    }

    public static class DelayElement implements Delayed {

        /**
         * 延迟时间, 单位:毫秒
         */
        long delayTime;

        public DelayElement(long delayTime) {
            this.delayTime = System.currentTimeMillis() + delayTime;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            // 将时间差转换成毫秒
            return unit.convert(delayTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(Delayed o) {
            // 延时队列的元素对象排序规则
            return Long.compare(this.getDelay(TimeUnit.MILLISECONDS), o.getDelay(TimeUnit.MILLISECONDS));
        }

        @Override
        public String toString() {
            // 格式化当前时间戳时间, yyyy-MM-dd HH:mm:ss
            return DateFormat.getDateTimeInstance().format(new Date(delayTime));
        }

    }
}
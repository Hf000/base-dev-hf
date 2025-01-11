package org.hf.application.javabase.algorithm;

import org.hf.application.javabase.utils.ThreadPoolUtil;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 限流算法之漏桶算法  伪代码实现
 * 此算法不适用持续请求接口的场景, 因为这样会导致
 * 需要考虑线程安全问题
 */
public class LeakyBucket {
    /**
     * 桶的容量
     */
    private final long capacity;
    /**
     * 水漏出的速度(每秒系统能处理的请求数)
     */
    private final long rate;
    /**
     * 当前水量(当前累积请求数)
     */
    private final AtomicLong water = new AtomicLong(0);

    public LeakyBucket(long capacity, long rate) {
        this.capacity = capacity;
        this.rate = rate;
    }

    /**
     * 根据水流速率模拟漏水
     */
    private void waterFlow() {
        water.set(Math.max(0, water.get() - rate));
    }

    public boolean isAllow() {
        // 如果桶中剩余的水量 < 桶的容量 就表示可以接受请求
        if (water.get() < capacity) {
            // 还能装水
            water.incrementAndGet();
            return true;
        } else {
            // 水满了，直接拒绝
            return false;
        }
    }

    @SuppressWarnings("all")
    public static void main(String[] args) throws InterruptedException {
        // 容量为2，流出速率为1
        LeakyBucket bucket = new LeakyBucket(2, 1);
        // 定义一个线程池模拟漏水
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        // 参数1:需要执行的任务,参数2:首次执行任务之前的延迟时间,参数3:两次连续任务之间的时间间隔,参数4:时间单位
        executor.scheduleAtFixedRate(bucket::waterFlow, 1, 1, TimeUnit.SECONDS);
        long reqStartTime = System.currentTimeMillis();
        // 模拟不同时间段的请求
        for (int i = 0; i < 10; i++) {
            // 模拟同一时间段的多个请求
            for (int j = -1; j < new Random().nextInt(3); j++) {
                if (bucket.isAllow()) {
                    System.out.println("请求通过, i = " + i + ", j = " + j);
                } else {
                    System.out.println("请求被拒绝, i = " + i + ", j = " + j);
                }
            }
            TimeUnit.SECONDS.sleep(1);
            System.out.println("---------------------------------------------");
        }
        // 模拟持续请求
        /*for (int i = 0; i < 20; i++) {
            long reqTime = System.currentTimeMillis();
            if (bucket.isAllow()) {
                System.out.println("请求通过" + i + ", 请求时间cost=" + (System.currentTimeMillis() - reqTime));
            } else {
                System.out.println("请求被拒绝" + i + ", 请求时间cost=" + (System.currentTimeMillis() - reqTime));
            }
            // 模拟请求间隔时间
            if (new Random().nextBoolean()) {
                TimeUnit.SECONDS.sleep(1);
                System.out.println("请求休眠了");
            }
        }*/
        System.out.println("请求完成cost=" + (System.currentTimeMillis() - reqStartTime));
        // 关闭线程池
        ThreadPoolUtil.closeJdkThreadPool(executor);
    }
}
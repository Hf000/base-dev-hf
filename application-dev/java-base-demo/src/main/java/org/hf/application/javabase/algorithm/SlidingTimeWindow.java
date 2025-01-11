package org.hf.application.javabase.algorithm;

import java.util.LinkedList;
import java.util.Random;

/**
 * 滑动时间窗实现: 限流算法之滑动时间窗算法的伪代码实现
 * 假设每秒的请求不能超过100，我们设置一个1s的时间窗口，时间窗口中共有10个小格子，
 * 每个格子记录100ms的请求数，每100毫秒移动一次，每次移动都需要记录当前服务请求数
 * 我这里就简单实现，只有一个计数器，最新的小窗口永远存储最新访问请求总数。当然也可以每个小窗口都有自己的计数器。
 * 需要考虑线程安全问题
 */
public class SlidingTimeWindow {

    /**
     * 服务器访问次数，可以放在redis中实现分布式系统访问
     */
    private Long count = 0L;
    /**
     * 滑动时间窗，使用linkedList来记录滑动窗口的10个格子
     */
    private final LinkedList<Long> slots = new LinkedList<>();

    @SuppressWarnings({"all"})
    public static void main(String[] args) throws InterruptedException {
        SlidingTimeWindow timeWindow = new SlidingTimeWindow();
        // 增加一个子线程用来计算时间范围内的请求数量
        new Thread(() -> {
            try {
                timeWindow.onCheck();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        // 模拟一直都有请求数，随机休眠几毫秒
        //noinspection InfiniteLoopStatement
        while (true) {
            // 限流校验
            timeWindow.count++;
            Thread.sleep(new Random().nextInt(15));
        }
    }

    @SuppressWarnings({"all"})
    private void onCheck() throws InterruptedException {
        //noinspection InfiniteLoopStatement
        while (true){
            // 把当前访问总数存入时间小窗口中
            slots.add(count);
            // 时间窗口的剔除操作, 这里就实现了窗口的滑动效果
            if (slots.size() > 10){
                slots.removeFirst();
            }
            // 最新的时间小窗口的数和最老的时间小窗口数进行比较，是否要限流
            Long lastCount = slots.peekLast();
            Long firstCount = slots.peekFirst();
            if (firstCount != null && lastCount != null && lastCount - firstCount > 100) {
                throw new RuntimeException("请求繁忙,请稍后重试");
            }
            // 100毫秒计算一次请求量
            Thread.sleep(100);
        }
    }
}
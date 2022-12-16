package org.hf.application.javabase.thread.lock;

import lombok.SneakyThrows;

import java.util.concurrent.locks.ReentrantLock;

/**
 * <p> 自定义计数器实现 </p >
 * @author HF
 * @date 2022-10-24
 **/
public class CustomCounter {

    /**
     * 计数器变量
     */
    private static int i = 0;

    /**
     * 获取计数器的方法
     * @return int
     */
    public int get() {
        return i;
    }

    /**
     * 声明锁对象
     * 公平锁: ReentrantLock lock = new ReentrantLock(true);
     * 默认是非公平锁: ReentrantLock lock = new ReentrantLock(false);
     */
    ReentrantLock lock = new ReentrantLock();

    /**
     * 实现方式一: 通过ReentrantLock加锁实现
     */
    public void inc1() {
        lock.lock();
        try {
            int j = get();
            Thread.sleep(100);
            j++;
            i = j;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 实现方式二: 通过synchronized加锁实现
     */
    public synchronized void inc2() {
        try {
            int j = get();
            Thread.sleep(100);
            j++;
            i = j;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings({"all"})
    @SneakyThrows
    public static void main(String[] args) {
        final CustomCounter counter = new CustomCounter();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
//                counter.inc1();
                counter.inc2();
            }).start();
        }
        Thread.sleep(3000);
        System.out.println(counter.i);
    }
}
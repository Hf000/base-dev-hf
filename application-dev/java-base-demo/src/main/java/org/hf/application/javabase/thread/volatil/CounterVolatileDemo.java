package org.hf.application.javabase.thread.volatil;

import lombok.SneakyThrows;

/**
 * <p> volatile实现计数器 </p >
 * @author HF
 * @date 2022-11-02
 **/
public class CounterVolatileDemo {

    private static volatile int i = 0;

    public static int get() {
        return i;
    }

    public static void inc() {
        // 这里需要控制时间, 得在子线程唤醒之前完成赋值
        i = get() + 1;
    }

    @SuppressWarnings({"all"})
    @SneakyThrows
    public static void main(String[] args) {
        for (int a = 0; a < 10; a++) {
            new Thread(() -> {
                try {
                    // 让线程休眠一下, 能够让变量i得到缓存同步
                    Thread.sleep(Thread.currentThread().getId() * 100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 让变量i加1
                inc();
            }).start();
        }
        Thread.sleep(5000);
        // 这里输出10, 也不能说明volatile保证原子性操作, 只是利用了程序运行的时间差
        System.out.println(i);
    }
}

/**
 * volatile的错误用法, volatile并不能保证原子性操作
 */
class BadVolatile {

    private static volatile int i = 0;

    public static int get() {
        return i;
    }

    public static void inc() {
        int j = get();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        i = j + 1;
    }

    @SuppressWarnings({"all"})
    @SneakyThrows
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                inc();
            }).start();
        }
        Thread.sleep(5000);
        // 理论上10才对。但是实际并不是10, 所以volatile并不能保证原子性操作
        System.out.println(i);
    }
}
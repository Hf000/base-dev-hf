package org.hf.application.javabase.thread.threadLocal;

/**
 * <p> ThreadLocal的demo </p>
 * 保证每一个线程有自己的副本
 * InheritableThreadLocal 实现父子线程的共享, 复制父线程的副本,而不是使用父线程的引用
 * TransmittableThreadLocal 解决线程池线程没有销毁而导致的变量获取问题,在执行任务的时候复制父线程的
 * ThreadLocal上下文, 原因: 是因为线程池线程没有销毁而导致线程没有重新初始化;
 * 注意点: treadLocal的使用一定要注意使用完后进行数据清理,避免线程污染
 * @author hufei
 * @date 2022/9/25 18:53
 */
public class ThreadLocalDemo {

    /**
     * 初始化threadLocal, 避免获取时为空,所以初始化一下,如果为空则返回初始值
     */
    public static ThreadLocal<Integer> threadLocal = ThreadLocal.withInitial(() -> 1);

    public static InheritableThreadLocal<Integer> inThreadLocal = new InheritableThreadLocal<>();

    public static void main(String[] args) {
        ThreadLocalDemo threadLocalCase = new ThreadLocalDemo();
        // 每个线程的副本变量互相不影响
        threadLocalCase.startThreads();
        // 父子线程共享
//        inheritableThreadLocalDemo();
        // 线程池单个线程导致的变量共享问题
//        inheritableThreadLocalDemoBug();
    }

    /**
     * 测试用于线程池单个线程而导致的获取变量问题,可以通过TransmittableThreadLocal解决
     * 原因: 是因为线程池线程没有销毁进而导致线程没有重新初始化; InheritableThreadLocal是复制了父线程的副本,而不是使用父线程的引用
     */
    @SuppressWarnings({"all"})
    private static void inheritableThreadLocalDemoBug() {
        inThreadLocal.set(1);
        threadLocal.set(2);
        new Thread(() -> {
            System.out.println("inheritableThreadLocal sub Thread " + inThreadLocal.get());
            // 这里获取的是初始值, 所以ThreadLocal对子线程不共享
            System.out.println("threadLocal sub Thread " + threadLocal.get());
        }).start();
        System.out.println("inheritableThreadLocal main Thread " + inThreadLocal.get());
        System.out.println("threadLocal main Thread " + threadLocal.get());
        inThreadLocal.remove();
        threadLocal.remove();
    }

    /**
     * InheritableThreadLocal测试demo
     */
    @SuppressWarnings({"all"})
    private static void inheritableThreadLocalDemo() {
        inThreadLocal.set(1);
        threadLocal.set(2);
        new Thread(() -> {
            System.out.println("inheritableThreadLocal sub Thread " + inThreadLocal.get());
            // 这里获取的是初始值, 所以ThreadLocal对子线程不共享
            System.out.println("threadLocal sub Thread " + threadLocal.get());
        }).start();
        System.out.println("inheritableThreadLocal main Thread " + inThreadLocal.get());
        System.out.println("threadLocal main Thread " + threadLocal.get());
        inThreadLocal.remove();
        threadLocal.remove();
    }

    @SuppressWarnings({"all"})
    private void startThreads() {
        Thread[] threads = new Thread[3];
        for (int i = 0; i < threads.length; i++) {
            //给每一个线程的threadlocal赋值
            threads[i] = new Thread(new TestRun(i));
        }
        //启动线程
        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }
    }

    private static class TestRun implements Runnable {
        int id;

        public TestRun(int i) {
            this.id = i;
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + ": start");
            // 如果上面不初始化, 这里获取的值就是null
            Integer value = threadLocal.get();
            System.out.println(Thread.currentThread().getName() + ", value: " + value + ", id: " + id);
            value += id;
            threadLocal.set(value);
            System.out.println(Thread.currentThread().getName() + ": " + threadLocal.get());
            // 用完之后记得remove移除掉, 否则可能会造成内存泄漏
            threadLocal.remove();
        }
    }
}
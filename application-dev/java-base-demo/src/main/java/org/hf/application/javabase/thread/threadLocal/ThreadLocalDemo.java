package org.hf.application.javabase.thread.threadLocal;

/**
 * <p> ThreadLocal的demo </p>
 * 保证每一个线程有自己的副本
 *
 * @author hufei
 * @date 2022/9/25 18:53
 */
public class ThreadLocalDemo {

    /**
     * 初始化threadLocal, 避免获取时为空,所以初始化一下,如果为空则返回初始值
     */
    public static ThreadLocal<Integer> threadLocal = ThreadLocal.withInitial(() -> 1);

    public static void main(String[] args) {
        ThreadLocalDemo threadLocalCase = new ThreadLocalDemo();
        // 每个线程的副本变量互相不影响
        threadLocalCase.startThreads();
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
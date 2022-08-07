package org.hf.application.javabase.apply.jdk8.threadLocal;

/**
 * 保证每一个线程有自己的副本
 */
public class ThreadLocalCase {

    //初始化threadlocal
    public static ThreadLocal<Integer> threadLocal = new ThreadLocal<Integer>(){
        @Override
        protected Integer initialValue() {
            return 1;
        }
    };

    public static void main(String[] args) {
        ThreadLocalCase threadLocalCase = new ThreadLocalCase();
        threadLocalCase.startThreads();
    }

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

    private class TestRun implements Runnable {
        int id;
        public TestRun(int i) {
            this.id=i;
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName()+": start");
            Integer value = threadLocal.get();
            value+=id;
            threadLocal.set(value);
            System.out.println(Thread.currentThread().getName()+": "+threadLocal.get());
        }
    }
}

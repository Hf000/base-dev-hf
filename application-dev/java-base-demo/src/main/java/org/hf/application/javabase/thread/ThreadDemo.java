package org.hf.application.javabase.thread;

import org.hf.application.javabase.utils.ThreadPoolUtil;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p> 线程demo </p >
 *
 * @author hufei
 * @date 2022/9/25 16:59
 */
@SuppressWarnings("all")
public class ThreadDemo {

    public static void main(String[] args) throws Exception {
        // 守护线程
//        guardianThread();
        // 钩子线程
//        hookThread();
        // 线程顺序执行
//        sequentialExecution();
        // run()方法和start()方法调用
        runOrStartMethod();
        System.out.println("测试结束");
    }

    /**
     * 测试线程调用run()和start()方法的区别
     * 1.调run()方法:
     * 1>会阻塞主线程, 因为run()方法只是一个普通方法,在当前线程中执行run()方法逻辑
     * 2>是我们自己现实的方法
     * 2.调start()方法:
     * 1>不会阻塞主线程, 因为start()方法是内置方法
     * 2>是java线程约定的内置方法,能够让run()在新的线程上下文中运行,新创建一个线程执行run()方法逻辑
     */
    private static void runOrStartMethod() {
        new Thread(() -> {
            System.out.println("run->" + Thread.currentThread().getName() + ": start");
            ThreadPoolUtil.threadSleep(2);
            System.out.println("run->" + Thread.currentThread().getName() + ": end");
        }).run();
        new Thread(() -> {
            System.out.println("start->" + Thread.currentThread().getName() + ": start");
            ThreadPoolUtil.threadSleep(2);
            System.out.println("start->" + Thread.currentThread().getName() + ": end");
        }).start();
    }

    /**
     * 让线程顺序执行
     *
     * @throws Exception 异常
     */
    private static void sequentialExecution() throws Exception {
        AtomicInteger i = new AtomicInteger();
        Thread t1 = new Thread(() -> {
            i.incrementAndGet();
            System.out.println("线程1执行" + i.get());
        });
        Thread t2 = new Thread(() -> {
            i.incrementAndGet();
            System.out.println("线程2执行" + i.get());
        });
        Thread t3 = new Thread(() -> {
            i.decrementAndGet();
            System.out.println("线程3执行" + i.get());
        });
        Thread t4 = new Thread(() -> {
            i.decrementAndGet();
            System.out.println("线程4执行" + i.get());
        });
        // 开始线程
        t1.start();
        // 等待线程结束
        t1.join();
        t2.start();
        t2.join();
        t3.start();
        t3.join();
        t4.start();
        t4.join();
        System.out.println(i.get());
    }

    /**
     * 钩子线程
     **/
    private static void hookThread() {
        //设置一个钩子线程 : 就是当JVM要退出的时候该线程就会被执行;
        Runtime.getRuntime().addShutdownHook(new Thread(() -> System.out.println("JVM 退出了")));
        System.out.println("主线程结束");
    }

    /**
     * 守护线程
     * 结论, 如果子线程不是守护线程, 那么父线程会等待子线程执行完成才结束;
     * 如果子线程是守护线程, 那么父线程执行完就结束, 如果此时子线程还没有执行完也会被结束掉.
     **/
    private static void guardianThread() {
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("子线程输出");
        });
        // 将线程设置为守护线程, 守护线程特征: 可以自动结束自己的声明周期, 而非守护线程不可以.
        t.setDaemon(true);
        t.start();
        try {
            //阻塞当前主线程
            t.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("父线程输出");
    }

}
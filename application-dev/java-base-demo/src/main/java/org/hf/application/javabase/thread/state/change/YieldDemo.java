package org.hf.application.javabase.thread.state.change;

/**
 * <p> 线程让出CPU的执行权demo </p >
 * 线程释放CPU执行权: yield()不释放锁,运行中转为就绪，让出cpu给大家去竞争。当然有可能自己又抢了回来
 * @author HF
 * @date 2022-11-25
 **/
public class YieldDemo {

    @SuppressWarnings({"all"})
    public static void main(String[] args) {
        byte[] lock = new byte[0];
        Thread t1 = new Thread(() -> {
            synchronized (lock) {
                System.out.println("start");
                // 让出CPU执行权,但是不释放锁
                Thread.yield();
                System.out.println("end");
            }
        });
        // t1线程调用yield()方法后,t2可以进行CPU执行权竞争,但是拿不到锁
        Thread t2 = new Thread(() -> {
            synchronized (lock) {
                System.out.println("t2");
            }
        });
        // t1线程调用yield()方法后,t3可以进行CPU执行权竞争,如果抢到执行权则可以执行,但不一定可以抢到
        Thread t3 = new Thread(() -> {
            System.out.println("t3");
        });
        /**
         * 多执行几次可以获取到不同的结果
         * t3会插队抢到执行权，但是t2不会，因为t2和t1共用一把锁而yield不会释放锁
         * t3不见得每次都能抢到。可能t1让出又抢了回去
         */
        t1.start();
        t2.start();
        t3.start();
    }
}
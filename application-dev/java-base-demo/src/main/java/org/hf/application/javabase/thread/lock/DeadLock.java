package org.hf.application.javabase.thread.lock;

/**
 * <p> 死锁demo </p >
 * 死锁的条件
 *  1.互斥使用，即资源只能独享，一个占了其他都必须等。
 *  2.不可抢占，资源一旦被占，就只能等待占有者主动释放，其他线程抢不走。
 *  3.贪婪占有，占着一把锁不释放，同时又需要申请另一把。
 *  4.循环等待，即存在等待环路，A → B → C → A。
 * 避免死锁
 *  1.合理搭配锁顺序，如果必须获取多个锁，我们就要考虑不同线程获取锁的次序搭配
 *  2.少用synchronized，多用Lock.tryLock方法并配置超时时间
 *  3.对多线程保持谨慎。拿不准的场景宁可不用。线上一旦死锁往往正是高访问时间段。代价巨大
 * @author HF
 * @date 2022-11-25
 **/
public class DeadLock {
    /**
     * 定义两个锁对象
     */
    final byte[] lock1 = new byte[0];
    final byte[] lock2 = new byte[0];

    public void f1() throws InterruptedException {
        // 获取lock1锁
        synchronized (lock1){
            System.out.println("f1.lock1");
            Thread.sleep(1000);
            // 获取lock2锁
            synchronized (lock2){
                System.out.println("f1.lock2");
            }
        }
    }

    public void f2() throws InterruptedException {
        // 获取lock2锁
        synchronized (lock2){
            System.out.println("f2.lock2");
            Thread.sleep(1000);
            // 获取lock1锁
            synchronized (lock1){
                System.out.println("f2.lock1");
            }
        }
    }

    @SuppressWarnings({"all"})
    public static void main(String[] args) {
        DeadLock deadLock = new DeadLock();
        // 这里会产生死锁,因为线程1获取到锁lock1后获取获取锁lock2, 此时锁lock2被线程2获取到,且线程2又去获取锁lock1,所以产生了死锁
        Thread t1 = new Thread(()->{
            try {
                deadLock.f1();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread t2 = new Thread(()->{
            try {
                deadLock.f2();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t1.start();
        t2.start();
    }
}

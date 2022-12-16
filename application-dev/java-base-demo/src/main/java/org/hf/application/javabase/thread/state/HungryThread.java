package org.hf.application.javabase.thread.state;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;

/**
 * <p> 饥饿线程 </p >
 * 一个线程因为 CPU 时间全部被其他线程抢走而始终得不到 CPU 运行时间
 * 产生原因:
 *  1.高优先级线程吞噬所有的低优先级线程的 CPU 时间。
 *  2.锁始终被别的线程抢占。
 * 解决方案:
 *  1.保证资源充足
 *  2.避免持有锁的线程长时间执行，设置一定的退出机制
 *  3.在高风险地方使用公平锁
 * @author HF
 * @date 2022-11-25
 **/
public class HungryThread {

    ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    void write() {
        readWriteLock.writeLock().lock();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("write");
        readWriteLock.writeLock().unlock();
    }

    void read() {
        readWriteLock.readLock().lock();
        System.out.println("read");
        readWriteLock.readLock().unlock();
    }

    @SuppressWarnings({"all"})
    public static void main(String[] args) {
        HungryThread hungryThread = new HungryThread();
        Thread t1 = new Thread(() -> {
            //不停去拿写锁，拿到后sleep一段时间，释放
            while (true) {
                hungryThread.write();
            }
        });
        Thread t2 = new Thread(() -> {
            //不停去拿读锁，虽然是读锁，但是此线程执行优先级低所以得不到执行权
            while (true) {
                hungryThread.read();
            }
        });
        t1.setPriority(9);
        //优先级低！
        t2.setPriority(1);
        // read几乎不会出现，甚至一直都拿不到锁。处于饥饿状态
        t1.start();
        t2.start();
    }
}

/**
 * <p> 解决上面饥饿线程问题 </p >
 * @author HF
 * @date 2022-11-25
 **/
class StampedThread {

    StampedLock lock = new StampedLock();

    void write() {
        long stamp = lock.writeLock();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("write");
        lock.unlock(stamp);
    }

    void read() {
        //乐观读
        long stamp = lock.tryOptimisticRead();
        //判断是否有写在进行，没占用的话，得到执行，打印read
        if (lock.validate(stamp)) {
            System.out.println("read");
        }
    }

    @SuppressWarnings({"all"})
    public static void main(String[] args) {
        StampedThread stampedThread = new StampedThread();
        Thread t1 = new Thread(() -> {
            while (true) {
                stampedThread.write();
            }
        });
        Thread t2 = new Thread(() -> {
            while (true) {
                stampedThread.read();
            }
        });
        t1.setPriority(9);
        t2.setPriority(1);
        // read间隔性打出，提升了读操作的并发性
        t1.start();
        t2.start();
    }
}

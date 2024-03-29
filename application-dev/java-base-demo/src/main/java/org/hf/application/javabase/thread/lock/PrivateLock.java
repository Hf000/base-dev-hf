package org.hf.application.javabase.thread.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * <p> 独享锁 </p >
 *
 * @author HF
 * @date 2022-10-24
 **/
@SuppressWarnings({"all"})
public class PrivateLock {

    /**
     * 方式一: ReentrantLock
     */
    Lock lock = new ReentrantLock();
    /**
     * 方式二: ReentrantReadWriteLock.WriteLock
     */
    ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();
    long start = System.currentTimeMillis();

    void read() {
//        lock.lock();
        writeLock.lock();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
//            lock.unlock();
            writeLock.unlock();
        }
        System.out.println("read time = " + (System.currentTimeMillis() - start));
    }

    public static void main(String[] args) {
        final PrivateLock lock = new PrivateLock();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                lock.read();
            }).start();
        }
    }
}
package org.hf.application.javabase.thread.lock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * <p> 共享锁 </p >
 *
 * @author HF
 * @date 2022-10-24
 **/
public class SharedLock {

    ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
    long start = System.currentTimeMillis();

    void read() {
        readLock.lock();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            readLock.unlock();
        }
        System.out.println("read time = " + (System.currentTimeMillis() - start));
    }

    @SuppressWarnings({"all"})
    public static void main(String[] args) {
        final SharedLock lock = new SharedLock();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                lock.read();
            }).start();
        }
    }
}
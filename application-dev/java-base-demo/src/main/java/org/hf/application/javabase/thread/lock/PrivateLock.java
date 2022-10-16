package org.hf.application.javabase.thread.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class PrivateLock {
    Lock lock = new ReentrantLock();
    ReentrantReadWriteLock lock2 = new ReentrantReadWriteLock();
    long start = System.currentTimeMillis();
    void read() {
        lock2.writeLock().lock();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock2.writeLock().unlock();
        }
        System.out.println("read time = "+(System.currentTimeMillis() - start));
    }

    public static void main(String[] args) {
        final PrivateLock lock = new PrivateLock();
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                public void run() {
                    lock.read();
                }
            }).start();
        }
    }
}
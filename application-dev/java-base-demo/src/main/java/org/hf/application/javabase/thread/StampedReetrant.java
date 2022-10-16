package org.hf.application.javabase.thread;

import java.util.concurrent.locks.StampedLock;

public class StampedReetrant {
    public static void main(String[] args) {
        StampedLock lock = new StampedLock();

        long stamp1 = lock.writeLock();
            System.out.println(1);

            long stamp2 = lock.writeLock();
                System.out.println(2);
            lock.unlock(stamp2);

        lock.unlock(stamp1);

    }
}

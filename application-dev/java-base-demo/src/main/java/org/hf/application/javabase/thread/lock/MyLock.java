package org.hf.application.javabase.thread.lock;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class MyLock extends AbstractQueuedSynchronizer {
    public MyLock(int count){
        setState(count);
    }

    @Override
    protected int tryAcquireShared(int arg) {
        for (; ; ) {
            int current = getState();
            int newCount = current - arg;
            if (newCount < 0 || compareAndSetState(current, newCount)) {
                return newCount;
            }
        }
    }

    @Override
    protected boolean tryReleaseShared(int arg) {
        for (; ; ) {
            int current = getState();
            int newState = current + arg;
            if (compareAndSetState(current, newState)) {
                return true;
            }
        }
    }


    public static void main(String[] args) {
        final MyLock lock = new MyLock(3);

        for (int i = 0; i < 30; i++) {
            new Thread(new Runnable() {
                public void run() {
                    lock.acquireShared(1);
                    try {
                        Thread.currentThread().sleep(1000);
                        System.out.println("ok");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        lock.releaseShared(1);
                    }

                }
            }).start();
        }
    }
}
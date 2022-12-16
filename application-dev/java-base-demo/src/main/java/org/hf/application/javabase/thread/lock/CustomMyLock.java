package org.hf.application.javabase.thread.lock;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * <p> 自定义锁,继承AQS </p >
 * @author HF
 * @date 2022-10-27
 **/
public class CustomMyLock extends AbstractQueuedSynchronizer {

    private static final long serialVersionUID = -3652271266899495805L;

    public CustomMyLock(int count){
        super.setState(count);
    }

    /**
     * 共享式获取同步状态，返回值大于等于0则表示获取锁成功，否则获取失败
     * @param arg 入参
     * @return int
     */
    @Override
    protected int tryAcquireShared(int arg) {
        for (;;) {
            int current = super.getState();
            int newCount = current - arg;
            if (newCount < 0 || super.compareAndSetState(current, newCount)) {
                return newCount;
            }
        }
    }

    /**
     * 共享式释放同步状态
     * @param arg 入参
     * @return boolean
     */
    @Override
    protected boolean tryReleaseShared(int arg) {
        for (;;) {
            int current = super.getState();
            int newState = current + arg;
            if (super.compareAndSetState(current, newState)) {
                return true;
            }
        }
    }

    @SuppressWarnings({"all"})
    public static void main(String[] args) {
        // 声明锁对象, 入参:一次可以有三个线程执行
        final CustomMyLock lock = new CustomMyLock(3);
        for (int i = 0; i < 30; i++) {
            new Thread(() -> {
                // 获取锁
                lock.acquireShared(1);
                try {
                    Thread.sleep(1000);
                    System.out.println("ok");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    // 释放锁
                    lock.releaseShared(1);
                }
            }).start();
        }
    }
}
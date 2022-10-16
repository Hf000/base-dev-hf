package org.hf.application.javabase.thread.buis;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class HungryThread{
    ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    void write(){
        readWriteLock.writeLock().lock();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        readWriteLock.writeLock().unlock();
    }
    void read(){
        readWriteLock.readLock().lock();
        System.out.println("read");
        readWriteLock.readLock().unlock();
    }

    public static void main(String[] args) {
        HungryThread hungryThread = new HungryThread();

        Thread t1 = new Thread(()->{
            //不停去拿写锁，拿到后sleep一段时间，释放
            while (true) {
                hungryThread.write();
            }
        });

        Thread t2 = new Thread(()->{
            //不停去拿读锁，虽然是读锁，但是...看下面！
            while (true){
                hungryThread.read();
            }
        });

        t1.setPriority(9);
        //优先级低！
        t2.setPriority(1);

        t1.start();
        t2.start();
    }
}

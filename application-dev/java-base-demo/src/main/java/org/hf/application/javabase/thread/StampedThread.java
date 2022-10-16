package org.hf.application.javabase.thread;

import java.util.concurrent.locks.StampedLock;

public class StampedThread {
    StampedLock lock = new StampedLock();
    void write(){
        long stamp = lock.writeLock();
        System.out.println(1);
        write2();
        lock.unlock(stamp);
    }
    void write2(){
        long stamp = lock.writeLock();
        System.out.println(2);
        lock.unlock(stamp);
    }
    void read(){
        //乐观读
        long stamp = lock.tryOptimisticRead();
        //判断是否有写在进行，没占用的话，得到执行，打印read
        if (lock.validate(stamp)){
            System.out.println("read");
        }
    }

    public static void main(String[] args) {
        StampedThread stampedThread = new StampedThread();

        Thread t1 = new Thread(()->{
            stampedThread.write();
        });

        t1.start();
    }
}

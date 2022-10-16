package org.hf.application.javabase.thread.lock;

import java.util.concurrent.locks.ReentrantLock;

public class NestedLock {
    public synchronized void f1(){
        System.out.println("f1");
    }
    public synchronized void f2(){
        f1();
        System.out.println("f2");
    }

    public static void main(String[] args) {
        NestedLock lock = new NestedLock();
        //可以正常打印 f1,f2
        lock.f2();

        ReentrantLock lock1 = new ReentrantLock(false);
    }
}
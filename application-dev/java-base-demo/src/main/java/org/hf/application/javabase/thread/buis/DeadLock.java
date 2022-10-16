package org.hf.application.javabase.thread.buis;

public class DeadLock {
    byte[] lock1 = new byte[0];
    byte[] lock2 = new byte[0];

    public void f1() throws InterruptedException {
        synchronized (lock1){
            System.out.println("f1.lock1");
            Thread.sleep(1000);
            synchronized (lock2){
                System.out.println("f1.lock2");
            }
        }
    }

    public void f2() throws InterruptedException {
        synchronized (lock2){
            System.out.println("f2.lock2");
            Thread.sleep(1000);
            synchronized (lock1){
                System.out.println("f2.lock1");
            }
        }

    }


    public static void main(String[] args) {
        DeadLock deadLock = new DeadLock();

        Thread t1 = new Thread(()->{
            try {
                deadLock.f1();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread t2 = new Thread(()->{
            try {
                deadLock.f2();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t1.start();
        t2.start();

    }

}

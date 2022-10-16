package org.hf.application.javabase.thread.buis;

public class YieldTest {
    public static void main(String[] args) {
        byte[] lock = new byte[0];

        Thread t1 = new Thread(()->{
            synchronized (lock){
                System.out.println("start");
                Thread.yield();
                System.out.println("end");
            }

        });
        Thread t2 = new Thread(()->{
            synchronized (lock){
                System.out.println("t2");
            }

        });
        Thread t3 = new Thread(()->{
                System.out.println("t3");
        });

        t1.start();
        t2.start();
        t3.start();



    }

}

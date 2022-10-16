package org.hf.application.javabase.thread.demo;

import java.util.concurrent.locks.LockSupport;

public class BlockTest {

    public static void main(String[] args) throws InterruptedException {
        byte[] lock = new byte[0];

        Thread t1 =  new Thread(()->{
            synchronized (lock){

                try {
                    LockSupport.park();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        });

        Thread t2 = new Thread(()->{
            synchronized (lock){
                System.out.println("t2");
            }

        });

        t2.setPriority(9);
        t1.setPriority(1);

        t1.start();
        t2.start();

        Thread.sleep(500);
        System.out.println(t1.getState());
        LockSupport.unpark(t1);
        Thread.sleep(500);
        System.out.println(t1.getState());




    }
}

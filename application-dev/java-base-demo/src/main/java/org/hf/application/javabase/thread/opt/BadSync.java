package org.hf.application.javabase.thread.opt;

import java.util.concurrent.atomic.AtomicLong;

public class BadSync implements Runnable{

    long start = System.currentTimeMillis();
    AtomicLong atomicLong = new AtomicLong(0);
    volatile int i=0;
    public  void inc(){
        i++;
    }

    @Override
    public synchronized void run() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        inc();
        atomicLong.getAndAdd(System.currentTimeMillis() - start);
    }
//    public  void run2() {
//        try {
//            Thread.sleep(100);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        synchronized(this){
//
//            inc();
//            totalTime += (System.currentTimeMillis() - start);
//        }
//
//    }


    public static void main(String[] args) throws InterruptedException {
        BadSync sync = new BadSync();

        for (int i = 0; i < 5; i++) {
            new Thread(sync).start();
        }


        Thread.sleep(3000);

        System.out.println("最终计数：i="+ sync.i);
        System.out.println("最终耗时：time="+sync.atomicLong.get());

    }
}

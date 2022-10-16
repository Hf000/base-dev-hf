package org.hf.application.javabase.thread.lock;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicCounter {
//    private static int i=0;
    private AtomicInteger i = new AtomicInteger(0);
    public int get(){
        return i.get();
    }
    public void inc(){
//        int j=get();
        try {
            Thread.sleep(100);
//            j++;
//            i=j;
//            i.incrementAndGet();
            i.getAndIncrement();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
        }

    }

    public static void main(String[] args) throws InterruptedException {
        final AtomicCounter counter = new AtomicCounter();
        //不使用线程10次，对比使用线程10次，看结果
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                public void run() {
                    counter.inc();
                }
            }).start();
        }


        Thread.sleep(3000);
        //理论上10才对。可是....
        System.out.println(counter.i);
    }
}
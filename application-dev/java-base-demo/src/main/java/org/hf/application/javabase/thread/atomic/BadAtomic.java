package org.hf.application.javabase.thread.atomic;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class BadAtomic {
    AtomicInteger i = new AtomicInteger(0);
    static int j=0;

    public synchronized void badInc(){
        int k = i.incrementAndGet();
        try {
            Thread.sleep(new Random().nextInt(100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        j=k;
    }

    public static void main(String[] args) throws InterruptedException {
        BadAtomic atomic = new BadAtomic();
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                atomic.badInc();
            }).start();
        }
        Thread.sleep(3000);
        System.out.println(atomic.j);
        System.out.println(atomic.i.get());
    }
}
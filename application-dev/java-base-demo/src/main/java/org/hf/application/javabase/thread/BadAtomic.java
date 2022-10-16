package org.hf.application.javabase.thread;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class BadAtomic {
    AtomicInteger i = new AtomicInteger(0);
    ThreadLocal<Integer> threadLocal = new ThreadLocal<>();
    static int j=0;

    public void badInc(){
        int k = i.incrementAndGet();
        try {
            Thread.currentThread().sleep(new Random().nextInt(100));
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
        Thread.sleep(2000);
        atomic.threadLocal.set(100);
        System.out.println(atomic.j);
    }
}
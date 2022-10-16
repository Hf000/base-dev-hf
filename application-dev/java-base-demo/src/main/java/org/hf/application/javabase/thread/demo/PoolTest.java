package org.hf.application.javabase.thread.demo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PoolTest {

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    System.out.println("t1");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        executor.shutdownNow();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("t2");
            }
        });



    }
}

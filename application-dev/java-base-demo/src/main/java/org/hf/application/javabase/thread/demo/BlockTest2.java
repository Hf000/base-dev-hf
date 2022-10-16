package org.hf.application.javabase.thread.demo;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class BlockTest2 {

    public static void main(String[] args) {

        ThreadPoolExecutor poolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
        poolExecutor.execute(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(50000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        System.out.println("ok");

    }
}

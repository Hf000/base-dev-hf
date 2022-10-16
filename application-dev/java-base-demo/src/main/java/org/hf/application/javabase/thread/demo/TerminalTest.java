package org.hf.application.javabase.thread.demo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TerminalTest {

    public static void main(String[] args) {

        ExecutorService poolExecutor = Executors.newFixedThreadPool(5);
        poolExecutor.execute(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(1000);
                    System.out.println("finish task 1");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        poolExecutor.shutdown();
        poolExecutor.execute(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        System.out.println("ok");
    }
}

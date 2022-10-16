package org.hf.application.javabase.thread.demo;

public class VolatileTest extends Thread {
    private static boolean flag = true;

    public void run() {
        while (flag) ;
        System.out.println("finish");
    }

    public static void main(String[] args) throws Exception {
        new VolatileTest().start();
        Thread.sleep(2000);
        flag = false;
    }
}
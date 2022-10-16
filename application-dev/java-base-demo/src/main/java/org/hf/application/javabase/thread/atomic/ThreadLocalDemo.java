package org.hf.application.javabase.thread.atomic;

public class ThreadLocalDemo implements Runnable{
    private static ThreadLocal<Integer> threadLocal = new ThreadLocal<>();
    public void run(){
        for (int i = 0; i < 3; i++) {
            threadLocal.set(i);
            System.out.println(Thread.currentThread().getName()+",value="+threadLocal.get());
        }
    }

    public static void main(String[] args) {
        ThreadLocalDemo demo = new ThreadLocalDemo();
        new Thread(demo).start();
        new Thread(demo).start();
    }


}
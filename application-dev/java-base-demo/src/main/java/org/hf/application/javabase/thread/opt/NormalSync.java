package org.hf.application.javabase.thread.opt;

public class NormalSync implements Runnable{
    Long start = System.currentTimeMillis();
    int i=0;

    public synchronized void run() {
        int j = i;
        //实际业务中可能会有一堆的耗时操作，这里等待100ms模拟
        try {
            //做一系列操作
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //业务结束后，增加计数
        i = j+1;
        System.out.println(Thread.currentThread().getId()+
                " ok,time="+(System.currentTimeMillis() - start));
    }

    public static void main(String[] args) throws InterruptedException {
        NormalSync test = new NormalSync();
        new Thread(test).start();
        new Thread(test).start();
        Thread.currentThread().sleep(1000);
        System.out.println("last value="+test.i);
    }
}    
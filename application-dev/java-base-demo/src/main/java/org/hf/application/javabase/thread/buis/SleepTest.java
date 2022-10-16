package org.hf.application.javabase.thread.buis;

public class SleepTest {

    public static void main(String[] args) throws InterruptedException {
        byte[] lock = new byte[0];

        Thread t1 = new Thread(()->{
            synchronized (lock){
                System.out.println("start");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("end");
            }


        });
        Thread t2 = new Thread(()->{
            synchronized (lock){
                System.out.println("t2");
            }
        });

        t1.start();
        Thread.sleep(100);
        t2.start();

    }


}

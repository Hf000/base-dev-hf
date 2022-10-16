package org.hf.application.javabase.thread.buis;

public class NotifyTest {

    public static void main(String[] args) {
        byte[] lock = new byte[0];


        Thread t1 = new Thread(()->{
            synchronized (lock){
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("t1");
            }
        });
        Thread t2 = new Thread(()->{
            synchronized (lock){
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("t2");
            }
        });
        Thread t3 = new Thread(()->{
            synchronized (lock){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("t3");
                lock.notifyAll();
            }
        });


        t2.setPriority(2);
        t1.setPriority(3);
        t3.setPriority(2);

        t1.start();
        t2.start();
        t3.start();

    }


}

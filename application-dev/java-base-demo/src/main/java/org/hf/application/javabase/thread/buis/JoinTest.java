package org.hf.application.javabase.thread.buis;

public class JoinTest implements Runnable{
    int i = 0;

    @Override
    public void run() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        i=1;
        System.out.println("sub");
    }

    public static void main(String[] args) throws InterruptedException {
        JoinTest test = new JoinTest();
        System.out.println("start");
        Thread t = new Thread(test);
        t.start();
        t.join();
        System.out.println(test.i);
        System.out.println("end");

    }
}

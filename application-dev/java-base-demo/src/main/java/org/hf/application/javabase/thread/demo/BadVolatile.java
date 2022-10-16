package org.hf.application.javabase.thread.demo;

public class BadVolatile {
    private static volatile int i=0;
    public int get(){
        return i;
    }
    public void inc(){
        int j=get();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        i=j+1;
    }

    public static void main(String[] args) throws InterruptedException {
        final BadVolatile counter = new BadVolatile();
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                public void run() {
                    counter.inc();
                }
            }).start();
        }
        Thread.sleep(3000);
        //理论上10才对。可是....
        System.out.println(counter.i);
    }
}
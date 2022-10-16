package org.hf.application.javabase.thread.lock;

public class ObjectLock {
    public static Integer i=0;
    public static synchronized void inc(){
            int j=i;
            try {
                Thread.sleep(100);
                j++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i=j;
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                public void run() {
                  //重点！
                    new ObjectLock().inc();
                }
            }).start();
        }
        Thread.sleep(3000);
        //理论上10才对。可是....
        System.out.println(ObjectLock.i);
    }
}
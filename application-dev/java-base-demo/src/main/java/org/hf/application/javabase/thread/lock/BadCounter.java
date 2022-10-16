package org.hf.application.javabase.thread.lock;

public class BadCounter {
    private static int i=0;
//    ReentrantLock lock = new ReentrantLock();
    public int get(){
        return i;
    }
    public void inc(){
//        lock.lock();
        int j=get();
        try {
            Thread.sleep(100);
            j++;
            i=j;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
//            lock.unlock();
        }

    }

    public static void main(String[] args) throws InterruptedException {
        final BadCounter counter = new BadCounter();
        //不使用线程10次，对比使用线程10次，看结果
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
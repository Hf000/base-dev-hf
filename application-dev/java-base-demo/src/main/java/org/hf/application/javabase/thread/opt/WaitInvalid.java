package org.hf.application.javabase.thread.opt;

public class WaitInvalid {
    volatile int total = 0;
    byte[] lock = new byte[0];

    //计算1-100的和，算完后通知print
    public void count(){
        synchronized (lock){
            for (int i = 1; i < 101; i++) {
                total += i;
            }
            lock.notify();
        }
        System.out.println("count finish");
    }
	//打印，等候count的通知
    public void print(){
        synchronized (lock){
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(total);
    }

    public static void main(String[] args) throws InterruptedException {
        WaitInvalid waitInvalid = new WaitInvalid();



        new Thread(()->{
            waitInvalid.count();
        }).start();

        Thread.sleep(1000);

        new Thread(()->{
            waitInvalid.print();
        }).start();

    }
}
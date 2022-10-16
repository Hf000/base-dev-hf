package org.hf.application.javabase.thread.lock;

public class BadLock{
    Lock lock = new Lock();
    public void f1(){
        System.out.println("f1");
        lock.lock();
        f2();
        lock.unlock();
    }
    public void f2(){
        lock.lock();
        System.out.println("f2");
        lock.unlock();
    }

    public static void main(String[] args) {
        BadLock badLock = new BadLock();
        //理论上，会打印 f1 和 f2 
        //实际上，这个错误的设计会导致卡死在f1
        badLock.f1();
    }

    //自定义的锁，现实中不要这么做！！！
    class Lock{
        private boolean isLocked = false;
        public synchronized void lock(){
            try {
                //想要拿锁，一直判断标记，如果被占就wait等待
                while(isLocked) {
                    wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //一旦被唤醒，退出while了，自己拿到锁，将标记改为true（已占用）
            isLocked = true;
        }
        public synchronized void unlock(){
            //占用标记改成false
            isLocked = false;
            //同时唤醒等待锁的线程
            notify();
        }
    }
}
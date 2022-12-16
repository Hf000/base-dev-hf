package org.hf.application.javabase.thread.lock;

/**
 * <p> 可重入锁demo </p >
 * 同步块内需要再次获取同一把锁的时候，直接放行，而不是等待, 防止死锁; synchronized 和ReentrantLock 都是可重入锁。
 *
 * @author HF
 * @date 2022-10-27
 **/
public class ReentrantLockDemo {

    public static void main(String[] args) {
        // 场景一: 父类和子类的锁的重入（调super方法）
//        parentSubReentrant();
        //场景二: 内嵌方法可重入
//        embeddedReentrant();
        // 可重入锁的错误用法
        wrongUsage();
    }

    /**
     * 错误用法
     */
    private static void wrongUsage() {
        BadLock badLock = new BadLock();
        //理论上，会打印 f1 和 f2
        //实际上，这个错误的设计会导致卡死在f1
        badLock.f1();
    }

    /**
     * 内嵌方法可重入
     */
    private static void embeddedReentrant() {
        NestedLock lock2 = new NestedLock();
        //可以正常打印 f1,f2
        lock2.f2();
    }

    /**
     * 父类和子类的锁的重入（调super方法）
     */
    private static void parentSubReentrant() {
        SonLock lock = new SonLock();
        lock.f1();
    }
}

/**
 * 错误实现
 */
class BadLock{

    /**
     * 声明锁对象
     */
    Lock lock = new Lock();

    public void f1() {
        System.out.println("f1");
        lock.lock();
        // 这里会造成死锁, f1()方法获取到锁后, f2()方法又去获取锁, 而此时f1()并没有释放锁, 如果把调用f2()移到释放锁之后调用则正常
        f2();
        lock.unlock();
    }

    public void f2() {
        lock.lock();
        System.out.println("f2");
        lock.unlock();
    }

    /**
     * 自定义的锁，现实中不要这么做！！！
     */
    static class Lock {

        /**
         * 声明锁是否被占用变量
         */
        private boolean isLocked = false;

        /**
         * 获取锁
         */
        public synchronized void lock() {
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

        /**
         * 释放锁
         */
        public synchronized void unlock(){
            //占用标记改成false
            isLocked = false;
            //同时唤醒等待锁的线程
            notify();
        }
    }
}

/**
 * 内嵌方法可重入
 */
class NestedLock {

    public synchronized void f1(){
        System.out.println("f1");
    }

    public synchronized void f2(){
        f1();
        System.out.println("f2");
    }
}

/**
 * 父类
 */
class ParentLock {
    /**
     * 声明一个锁对象
     */
    final byte[] lock = new byte[0];

    public void f1(){
        synchronized (lock){
            System.out.println("f1 from parent");
        }
    }
}
/**
 * 子类
 */
class SonLock extends ParentLock {

    @Override
    public void f1() {
        synchronized (super.lock){
            super.f1();
            System.out.println("f1 from son");
        }
    }
}
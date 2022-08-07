package org.hf.application.javabase.lock;

/**
 * @Author:hufei
 * @CreateTime:2020-07-31
 * @Description:测试Thread中的join方法
 */
public class JoinTest {

    public static void main(String[] args) throws InterruptedException {
        byte[] lock = new byte[0];  //先创建一把锁

        Thread t1 = new Thread(()->{
//            try {
//                Thread.currentThread().sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("sub01");
            }
        });//创建子线程t1

        Thread t2 = new Thread(()->{
//            try {
//                Thread.currentThread().sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            synchronized (lock) {
                System.out.println("sub02");
                lock.notify();
            }
        });//创建子线程t2

        t1.start();
        t2.start();
        System.out.println("parent start");

        t1.join();

        System.out.println("parent end");
    }
//    public static void main(String[] args) throws InterruptedException {
//        byte[] lock = new byte[0];  //先创建一把锁
//
//        Thread t1 = new Thread(()->{
//            try {
//                Thread t2= new Thread(() -> {
//                    try {
//                        Thread.currentThread().sleep(3000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    System.out.println("sub02");
//                });
//                t2.start();
//                t2.join();
//                Thread.currentThread().sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            System.out.println("sub");
//        });//创建子线程t1
//        t1.start();
//        System.out.println("parent start");
//
//        t1.join();
//
//        System.out.println("parent end");
//    }

}

package org.hf.application.javabase.thread.state.change;

/**
 * <p> 线程暂停demo </p >
 * 线程暂停: sleep()不会释放锁, 只是让出CPU执行权
 * @author HF
 * @date 2022-11-25
 **/
public class SleepDemo {

    @SuppressWarnings({"all"})
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
        // 线程1获取到锁后sleep()了, 所以其他线程无法获取到锁只能等待
        t1.start();
        Thread.sleep(100);
        t2.start();
    }
}

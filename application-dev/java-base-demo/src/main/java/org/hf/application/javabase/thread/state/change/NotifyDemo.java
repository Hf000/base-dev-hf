package org.hf.application.javabase.thread.state.change;

/**
 * <p> 线程唤醒demo </p >
 * 线程等待: wait()会释放锁
 * @author HF
 * @date 2022-11-25
 **/
public class NotifyDemo {

    @SuppressWarnings({"all"})
    public static void main(String[] args) {
        byte[] lock = new byte[0];
        Thread t1 = new Thread(()->{
            synchronized (lock){
                try {
                    // wait会释放锁
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
                    // wait会释放锁
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
                // 唤醒线程, 并将锁还给等待的线程, 唤醒的线程先看优先级,同级就是随机唤醒; 拓展:concurrent.lock中， Condition.await()，signal/signalAll 与 wait/notify效果一样
                lock.notifyAll();
            }
        });
        // 设置线程的运行的优先级, 1-10, 越大优先级越高, 5是默认值
        t2.setPriority(3);
        t1.setPriority(1);
        t3.setPriority(2);
        // 执行线程, wait让出锁，t3得到执行，t3唤醒后，虽然t1先start，但是优先级低，所以t2优先执行
        t1.start();
        t2.start();
        t3.start();
    }
}
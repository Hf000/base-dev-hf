package org.hf.application.javabase.thread.lock.invalid;

/**
 * <p> 锁等待失效demo </p >
 * @author HF
 * @date 2022-11-25
 **/
public class WaitInvalid {

    volatile int total = 0;
    /**
     * 定义一个锁对象
     */
    final byte[] lock = new byte[0];

    /**
     * 计算1‐100的和，算完后通知print
     */
    public void count() {
        synchronized (lock) {
            for (int i = 1; i < 101; i++) {
                total += i;
            }
            lock.notify();
        }
        System.out.println("count finish");
    }

    /**
     * 打印，等候count的通知
     */
    public void print() {
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(total);
    }

    @SuppressWarnings({"all"})
    public static void main(String[] args) {
        WaitInvalid waitInvalid = new WaitInvalid();
        /**
         * 按照这个顺序执行, 会陷入程序卡死
         * 原因:count()方法先执行时，提前释放了notify通知，这时候，print()方法还没进入wait，收不到这个信号, 再等通知等不到了，典型的通知过时现象。
         * 解决:如果将count()方法和print()方法的执行顺序对换一下就能解决问题了
         */
        new Thread(() -> waitInvalid.count()).start();
        new Thread(() -> waitInvalid.print()).start();
    }
}
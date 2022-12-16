package org.hf.application.javabase.thread.lock.optimization;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * <p> 锁优化demo, 采用CAS方式优化 </p >
 * @author HF
 * @date 2022-11-24
 **/
@SuppressWarnings({"all"})
public class CasSync implements Runnable {

    long start = System.currentTimeMillis();
    int i = 0;

    /**
     * 注意 这里两种方式的时间差不多的
     * 因为这里的 synchronized 锁住的操作是在内存中取值赋值, 而CAS也是执行的内存取值赋值操作, 从操作层面上讲是一个量级的, 所以这个时间不一定.
     * 也可能CAS的时间会短一点.这是锁的粒度问题导致的
     * 实际业务场景中, 一般会有一定的业务操作, 所以CAS的方式效率会更高
     */
    @Override
    public void run() {
        int j = i;
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //方式一: CAS处理，在这里理解思想，实际中不推荐大家使用！
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            Unsafe unsafe = (Unsafe) f.get(null);
            long offset = unsafe.objectFieldOffset(CasSync.class.getDeclaredField("i"));
            while (!unsafe.compareAndSwapInt(this,offset,j,j+1)){
                j = i;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 方式二: 实际开发中，要用atomic包，或者while+synchronized自旋
//        synchronized (this){
//            //注意这里！
//            while (j != i){
//                j = i;
//            }
//            i = j+1;
//        }
        System.out.println(Thread.currentThread().getName() + " ok,time=" + (System.currentTimeMillis() - start));
    }

    public static void main(String[] args) throws InterruptedException {
        CasSync test = new CasSync();
        new Thread(test).start();
        new Thread(test).start();
        new Thread(test).start();
        new Thread(test).start();
        new Thread(test).start();
        Thread.sleep(1000);
        System.out.println("last value=" + test.i);
    }
}

/**
 * <p> 锁未优化demo, 直接使用synchronized加锁 </p >
 * @author HF
 * @date 2022-11-24
 **/
class NormalSync implements Runnable {

    Long start = System.currentTimeMillis();
    int i = 0;

    @Override
    public synchronized void run() {
        int j = i;
        //实际业务中可能会有一堆的耗时操作，这里等待100ms模拟
        try {
            //做一系列操作
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //业务结束后，增加计数
        i = j + 1;
        System.out.println(Thread.currentThread().getId() + " ok,time=" + (System.currentTimeMillis() - start));
    }

    @SuppressWarnings({"all"})
    public static void main(String[] args) throws InterruptedException {
        NormalSync test = new NormalSync();
        new Thread(test).start();
        new Thread(test).start();
        Thread.sleep(1000);
        System.out.println("last value=" + test.i);
    }
}
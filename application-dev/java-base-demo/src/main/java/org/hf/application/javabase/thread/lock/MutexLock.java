package org.hf.application.javabase.thread.lock;

import lombok.SneakyThrows;

/**
 * <p> 互斥锁 </p >
 *
 * @author HF
 * @date 2022-10-27
 **/
public class MutexLock {

    public static Integer i = 0;

    public void inc() {
        // 这里锁住的原因: 调用此方法时每次都是new的新对象, 这里this不是代表统一个对象所以锁不住
        synchronized (this) {
            int j = i;
            try {
                Thread.sleep(100);
                j++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i = j;
        }
    }

    /**
     * 这里要声明成静态的方法, 不然每次new的对象调用方法,引用也不是同一个
     */
    public static synchronized void inc2() {
        int j = i;
        try {
            Thread.sleep(100);
            j++;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        i = j;
    }

    @SuppressWarnings({"all"})
    @SneakyThrows
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                //重点！ 这里锁不住
//                new MutexLock().inc();
                // 这里可以锁住
                new MutexLock().inc2();
            }).start();
        }
        Thread.sleep(3000);
        //理论上10才对。可是....
        System.out.println(MutexLock.i);
    }
}
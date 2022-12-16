package org.hf.application.javabase.thread.atomic;

import lombok.SneakyThrows;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p> 原子操作边界被破坏 </p >
 *
 * @author HF
 * @date 2022-10-28
 **/
public class BadAtomic {

    AtomicInteger i = new AtomicInteger(0);
    static int j = 0;

    @SneakyThrows
    public void badInc() {
        int k = i.incrementAndGet();
        Thread.sleep(new Random().nextInt(100));
        j = k;
    }

    @SneakyThrows
    @SuppressWarnings({"all"})
    public static void main(String[] args) {
        BadAtomic atomic = new BadAtomic();
        for (int i = 0; i < 10; i++) {
            new Thread(atomic::badInc).start();
        }
        Thread.sleep(3000);
        // 这个结果没问题, 此操作是属于原子操作
        System.out.println(atomic.i.get());
        /**
         * 这里每次获取的结果不一致
         * 原因: 对于类成员变量j来说,此场景哪个线程先获取到cpu执行那么就会执行哪个,导致j赋的值的AtomicInteger变量不一定是最终的值
         * 解决办法: 在badInc()方法上加synchronized
         */
        System.out.println(BadAtomic.j);
    }
}
package org.hf.application.javabase.thread.atomic;

import lombok.SneakyThrows;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p> 原子操作计数器 </p >
 * 原子操作类
 * 基本类型
 *     AtomicBoolean：以原子更新的方式更新boolean；
 *     AtomicInteger：以原子更新的方式更新Integer;
 *     AtomicLong：以原子更新的方式更新Long；
 * 引用类型
 *     AtomicReference ： 原子更新引用类型
 *     AtomicReferenceFieldUpdater ：原子更新引用类型的字段
 *     AtomicMarkableReference ： 原子更新带有标志位的引用类型
 * 数组
 *     AtomicIntegerArray：原子更新整型数组里的元素。
 *     AtomicLongArray：原子更新长整型数组里的元素。
 *     AtomicReferenceArray：原子更新引用类型数组里的元素。
 * 字段
 *     AtomicIntegerFieldUpdater：原子更新整型的字段的更新器。
 *     AtomicLongFieldUpdater：原子更新长整型字段的更新器。
 *     AtomicStampedReference：原子更新带有版本号的引用类型。
 *
 * @author HF
 * @date 2022-10-28
 **/
@SuppressWarnings({"all"})
public class AtomicCounter {

    /**
     * 定义计数器初始对象
     */
    private final AtomicInteger i = new AtomicInteger(0);

    /**
     * 获取计数器计数的值
     * @return int
     */
    public int get(){
        return i.get();
    }

    /**
     * 计数
     */
    public void inc(){
        try {
            Thread.sleep(100);
            // 先赋值再+1, 类似于i++
//            i.getAndIncrement();
            // 先加1再赋值, 类似于++i
            i.incrementAndGet();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    public static void main(String[] args) {
        final AtomicCounter counter = new AtomicCounter();
        // 计数操作
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                counter.inc();
            }).start();
        }
        Thread.sleep(3000);
        System.out.println(counter.i.get());
    }
}
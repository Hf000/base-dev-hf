package org.hf.application.javabase.thread.volatil;

import lombok.SneakyThrows;

/**
 * <p> Volatile关键字demo </p >
 * 解决多线程场景下的可见性和有序性问题
 *  可见性，是指线程之间的可见性，一个线程修改的状态对另一个线程是可见的
 *  有序性, 指的是你写的代码的顺序要和最终执行的指令保持一致。因为在Java内存模型中，允许编译器和处理器对指令进行重排序，
 *  重排序过程不会影响到单线程程序的执行，却会影响到多线程并发执行的正确性（这里涉及到JVM内存屏障的概念）。
 * 对变量加上volatile关键字,当对此变量进行写操作时,JVM会向处理器发送一条lock前缀指令,将缓存中的变量回写到主存中去（这里涉及到计算机的三级缓存机制）
 *
 * @author HF
 * @date 2022-10-31
 **/
@SuppressWarnings({"all"})
public class VolatileDemo {

    /**
     * 这里加volatile, 那么子线程中调用method就会读取到主线程重新赋值flag变量后的值, 不加就读取不到
     */
    private static volatile boolean flag = true;

    @SneakyThrows
    public static void main(String[] args) {
        new Thread(() -> {
            method();
        }).start();
        Thread.sleep(2000);
        // 这里置为false后,线程停止了
        flag = false;
        System.out.println("main finish");
    }

    private static void method() {
        long start = System.currentTimeMillis();
        while (flag) {
            // 这里不要有内容, 因为这里如果有逻辑会让while判断一个重新判断的时间间隔,可能就会读取到缓存同步之后的值
        }
        System.out.println(Thread.currentThread().getName() + "finish : " + (System.currentTimeMillis() - start));
    }
}
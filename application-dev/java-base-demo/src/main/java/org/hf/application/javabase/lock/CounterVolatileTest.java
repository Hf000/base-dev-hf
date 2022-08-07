package org.hf.application.javabase.lock;

/**
 * @Author:hufei
 * @CreateTime:2020-08-05
 * @Description:计数器, volatile测试
 */
public class CounterVolatileTest {

    private static volatile int i = 0;

    public int get() {
        return i;
    }

    public void inc() {
        int j = get();
//        try {
//            Thread.sleep(100);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        i = j + 1;
    }

    public static void main(String[] args) throws InterruptedException {
        CounterVolatileTest test = new CounterVolatileTest();
        for (int a = 0; a < 10; a++) {
            new Thread(() -> {
                try {
                    Thread.sleep(Thread.currentThread().getId()*100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                test.inc();   // lambda 方法体类的对象如果不是 final 修饰, jdk编译的时候会将这个对象转换成 final 修饰
//                test = new CounterVolatileTest();
            }).start();
        }
        Thread.sleep(5000);
        System.out.println(test.i);
    }

}

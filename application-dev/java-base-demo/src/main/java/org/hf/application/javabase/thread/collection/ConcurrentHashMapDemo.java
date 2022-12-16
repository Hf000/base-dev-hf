package org.hf.application.javabase.thread.collection;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p> ConcurrentHashMap的Demo </p >
 * jdk1.7版本采用的是分段锁, jdk1.8版本采用的是cas + synchronized方式提升操作效率
 * 锁:
 * 执行put的时候采用cas方式自旋, 为null表示可执行插入操作, 去寻找指定的插槽
 * 根据生成的key去找到对应的插槽, synchronized锁的是插槽上的头节点
 * 临界点: 节点达到临界点则转化为红黑树,按照树的规则放入Node,小于临界点则转换成链表
 *
 * @author HF
 * @date 2022-11-02
 **/
public class ConcurrentHashMapDemo {

    @SuppressWarnings({"all"})
    public static void main(String[] args) throws InterruptedException {
        //定义ConcurrentHashMap
        Map<String, String> map = new ConcurrentHashMap<>();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                //多线程下的put可以放心使用
                map.put(UUID.randomUUID().toString(), "1");
            }).start();
        }
        Thread.sleep(3000);
        System.out.println(map);
    }
}

/**
 * ConcurrentHashMap线程安全问题: concurrentHashMap是线程安全,但是不能保证原子性操作.
 */
class BadConcurrentHashMapDemo {

    @SuppressWarnings({"all"})
    public static void main(String[] args) throws InterruptedException {
        Map<String, Integer> map = new ConcurrentHashMap<>();
        map.put("val", 0);
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                // 这里可能十个线程都取到的是0
                int v = map.get("val");
                // 自加后为1
                v++;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 所以这里可能都是put的值为1
                map.put("val", v);
            }).start();
        }
        Thread.sleep(3000);
        System.out.println(map);
    }
}
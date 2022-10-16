package org.hf.application.javabase.thread.opt;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadAndWrite {
    //类创建的时间
    final long start = System.currentTimeMillis();
    //总耗时
    AtomicLong totalTime = new AtomicLong(0);
    //缓存变量
    private Map<String,Long> map = new ConcurrentHashMap(){{put("count",0L);}};
    ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    //查看map被写入了多少次
    public Map read(){
        lock.readLock().lock();
        try {
            Thread.currentThread().sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        //最后操作完成的时间
        map.put("time",end);
        lock.readLock().unlock();
        System.out.println(Thread.currentThread().getName()+",read="+(end-start));
        totalTime.addAndGet(end - start);
        return map;
    }
    //写入
    public Map write(){
        lock.writeLock().lock();
        try {
            Thread.currentThread().sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //写入计数
        map.put("count",map.get("count")+1);
        long end = System.currentTimeMillis();
        map.put("time",end);
        lock.writeLock().unlock();
        System.out.println(Thread.currentThread().getName()+",write="+(end-start));
        totalTime.addAndGet(end - start);
        return map;
    }


    public static void main(String[] args) throws InterruptedException {

        ReadAndWrite count = new ReadAndWrite();

        //读
        for (int i = 0; i < 9; i++) {
            new Thread(()->{
                count.read();
            }).start();
        }
        //写
        for (int i = 0; i < 1; i++) {
            new Thread(()->{
                count.write();
            }).start();
        }

        Thread.sleep(3000);
        System.out.println(count.map);
        System.out.println("读写总共耗时："+count.totalTime.get());
    }
}

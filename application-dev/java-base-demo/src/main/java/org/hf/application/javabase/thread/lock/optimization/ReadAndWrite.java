package org.hf.application.javabase.thread.lock.optimization;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * <p> 锁优化demo </p >
 * 这里的应用场景, 读操作可以加共享锁提升效率的, 写操作才需要加独享锁
 * @author HF
 * @date 2022-11-24
 **/
public class ReadAndWrite {
    /**
     * 类创建的时间
     */
    final long start = System.currentTimeMillis();
    /**
     * 总耗时
     */
    AtomicLong totalTime = new AtomicLong(0);
    /**
     * 缓存变量，注意！因为read方法并发且该方法中有写map的操作，这里换成ConcurrentHashMap
     */
    private final Map<String,Long> map = new ConcurrentHashMap<String,Long>(){{put("count",0L);}};
    /**
     * 读写锁, 读共享锁, 写独享锁
     */
    ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * 查看map被写入了多少次
     */
    public void read(){
        long end = 0;
        lock.readLock().lock();
        try {
            Thread.sleep(100);
            end = System.currentTimeMillis();
            //最后操作完成的时间
            map.put("time",end);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.readLock().unlock();
        }
        System.out.println(Thread.currentThread().getName()+",read="+(end-start));
        totalTime.addAndGet(end - start);
    }

    /**
     * 写入
     */
    public void write(){
        long end = 0;
        lock.writeLock().lock();
        try {
            Thread.sleep(100);
            //写入计数
            map.put("count",map.get("count")+1);
            end = System.currentTimeMillis();
            map.put("time",end);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }
        System.out.println(Thread.currentThread().getName()+",write="+(end-start));
        totalTime.addAndGet(end - start);
    }

    @SuppressWarnings({"all"})
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

/**
 * 锁未优化demo
 */
class TotalLock {
    /**
     * 类创建的时间
     */
    final long start = System.currentTimeMillis();
    /**
     * 总耗时
     */
    AtomicLong totalTime = new AtomicLong(0);
    /**
     * 缓存变量
     */
    private final Map<String, Long> map = new HashMap<String, Long>() {{
        put("count", 0L);
    }};
    /**
     * 独享锁
     */
    ReentrantLock lock = new ReentrantLock();

    /**
     * 查看map被写入了多少次
     */
    public void read() {
        long end = 0;
        lock.lock();
        try {
            Thread.sleep(100);
            end = System.currentTimeMillis();
            //最后操作完成的时间
            map.put("time", end);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        System.out.println(Thread.currentThread().getName() + ",read=" + (end - start));
        totalTime.addAndGet(end - start);
    }

    /**
     * 写入
     */
    public void write() {
        long end = 0;
        lock.lock();
        try {
            Thread.sleep(100);
            //写入计数
            map.put("count", map.get("count") + 1);
            end = System.currentTimeMillis();
            map.put("time", end);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        System.out.println(Thread.currentThread().getName() + ",write=" + (end - start));
        totalTime.addAndGet(end - start);
    }

    @SuppressWarnings({"all"})
    public static void main(String[] args) throws InterruptedException {
        TotalLock count = new TotalLock();
        //读, 这里读的时候都加了锁, 所以按照顺序读取, 所以这里可以优化,读的时候没有必要加独享锁
        for (int i = 0; i < 9; i++) {
            new Thread(() -> {
                count.read();
            }).start();
        }
        //写
        for (int i = 0; i < 1; i++) {
            new Thread(() -> {
                count.write();
            }).start();
        }
        Thread.sleep(3000);
        System.out.println(count.map);
        System.out.println("读写总共耗时：" + count.totalTime.get());
    }
}
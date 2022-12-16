package org.hf.application.javabase.thread.lock.invalid;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> 唤醒状态失效demo </p >
 * @author HF
 * @date 2022-11-25
 **/
public class NotifyInvalid {

    List<Integer> list = new ArrayList<>();
    /**
     * 声明一个锁对象
     */
    final byte[] lock = new byte[0];

    /**
     * 元素移除
     */
    public void del() {
        synchronized (lock){
            //没值就等，有值就删
            if (list.isEmpty()){
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 防止唤醒后状态失效, 这里需要多加一个状态判断
            if (list.isEmpty()) {
                System.out.println(Thread.currentThread().getName() + "暂无删除元素");
                return;
            }
            list.remove(0);
            System.out.println(Thread.currentThread().getName() + "移除成功");
        }
    }

    /**
     * 添加元素
     */
    public void add(){
        synchronized (lock){
            //加个值后唤醒
            list.add(0,0);
            lock.notifyAll();
        }
    }

    @SuppressWarnings({"all"})
    public static void main(String[] args) throws InterruptedException {
        NotifyInvalid notifyInvalid = new NotifyInvalid();
        /**
         * 出异常了！因为等候的两个线程第一个删除后，第二个唤醒时，等待前的状态已失效。
         * 解决: 线程唤醒后，要警惕睡眠前后状态不一致，要二次判断
         */
        //启动两个线程等候删除
        for (int i = 0; i < 2; i++) {
            new Thread(() -> notifyInvalid.del()).start();
        }
        //新线程添加一个
        new Thread(() -> notifyInvalid.add()).start();
        Thread.sleep(1000);
        System.out.println(notifyInvalid.list.size());
    }
}
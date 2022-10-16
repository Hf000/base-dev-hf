package org.hf.application.javabase.thread.opt;

import java.util.ArrayList;
import java.util.List;

public class NotifyInvalid {
    List list = new ArrayList();
    byte[] lock = new byte[0];

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
            if (list.isEmpty()){
                list.remove(0);
            }
        }
    }

    public void add(){
        synchronized (lock){
            //加个值后唤醒
            list.add(0,0);
            lock.notifyAll();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        NotifyInvalid notifyInvalid = new NotifyInvalid();

        //启动两个线程等候删除
        for (int i = 0; i < 2; i++) {
            new Thread(()->{
                notifyInvalid.del();
            }).start();
        }

        //新线程添加一个
        new Thread(()->{
            notifyInvalid.add();
        }).start();

        Thread.sleep(1000);

        System.out.println(notifyInvalid.list.size());
    }


}
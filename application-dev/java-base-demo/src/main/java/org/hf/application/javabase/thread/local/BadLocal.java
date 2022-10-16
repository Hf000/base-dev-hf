package org.hf.application.javabase.thread.local;

import java.util.HashMap;
import java.util.Map;

public class BadLocal{

    public static void main(String[] args) {
        ThreadLocal<Map> local = new ThreadLocal();
        Map map = new HashMap();
        new Thread(()->{
            //在线程设置后，过段时间取name
 			//猜一猜结果？
            map.put("name","i am "+Thread.currentThread().getName());
            local.set(map);
            System.out.println(Thread.currentThread().getName()+":"
                    +local.get().get("name"));
            //do something...
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+":"
                               +local.get().get("name"));
        }).start();

        new Thread(()->{
            //在线程中赋值name
            map.put("name","i am "+Thread.currentThread().getName());
            local.set(map);
        }).start();
    }

}
package org.hf.application.javabase.thread.threadLocal;

import java.util.HashMap;
import java.util.Map;

/**
 * <p> ThreadLocal的错误使用demo </p >
 * @author HF
 * @date 2022-11-22
 **/
public class BadLocal {

    @SuppressWarnings({"all"})
    public static void main(String[] args) {
        ThreadLocal<Map<String, String>> local = new ThreadLocal<>();
        Map<String, String> map = new HashMap<>();
        new Thread(() -> {
            // 在线程设置后，过段时间取name
            map.put("name","i am "+Thread.currentThread().getName());
            local.set(map);
            System.out.println(Thread.currentThread().getName()+":" + local.get().get("name"));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 这里取值的时候,线程隔离失效了,原因:下面线程中的map和这里指向同一个引用,会打破隔离而失效
            System.out.println(Thread.currentThread().getName()+":" + local.get().get("name"));
        }).start();
        new Thread(()->{
            // 在线程中赋值name, 这里的map和上面线程中的map是同一个引用
            map.put("name","i am "+Thread.currentThread().getName());
            local.set(map);
        }).start();
    }
}
package org.hf.boot.springboot.config;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author:hufei
 * @CreateTime:2020-11-16
 * @Description:将数据源设置到当前的线程中
 * 自定义实现多数据源 - 4
 */
public class DBContextHolder {
    private static final ThreadLocal<DBType> contextHolder = new ThreadLocal<>();

    private static final AtomicInteger counter = new AtomicInteger(-1);

    public static void set(DBType dbType) {
        contextHolder.set(dbType);
    }

    public static DBType get() {
        return contextHolder.get();
    }

    public static void master() {
        set(DBType.MASTER);
        System.out.println("切换到master");
    }

    public static void slave() {
        //  轮询
//        int index = counter.getAndIncrement() % 2;
//        if (counter.get() > 9999) {
//            counter.set(-1);
//        }
        set(DBType.SLAVE1);
        System.out.println("切换到slave1");
    }
}

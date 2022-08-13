package org.hf.application.mybatis.multiple.datasource.config;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p> 将数据源设置到当前的线程中 </p>
 * 自定义实现多数据源 - 4
 * @author hufei
 * @date 2022/8/13 9:46
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

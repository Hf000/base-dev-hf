package org.hf.application.javabase.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author:hufei
 * @CreateTime:2020-09-15
 * @Description:线程池测试
 */
public class ThreadPoolTest {

    private static void testThreadPool() {
        ExecutorService service = Executors.newFixedThreadPool(5);  //创建线程池
    }

}

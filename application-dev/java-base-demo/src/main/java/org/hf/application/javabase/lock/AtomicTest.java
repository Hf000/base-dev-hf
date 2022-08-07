package org.hf.application.javabase.lock;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author:hufei
 * @CreateTime:2020-09-15
 * @Description:Atomic测试
 */
public class AtomicTest {

    private static void testAtomic() {
        AtomicInteger i = new AtomicInteger(0);
        for (int j = 0; j < 10; j++) {
            i.incrementAndGet();
        }
        System.out.println(i);
    }

}

package org.hf.application.javabase.lock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author:hufei
 * @CreateTime:2020-09-15
 * @Description:ReentrantLock锁测试
 */
public class ReentrantLockTest {

    private static void testLock() {
        ReentrantLock lock = new ReentrantLock();
        lock.lock();
    }

}

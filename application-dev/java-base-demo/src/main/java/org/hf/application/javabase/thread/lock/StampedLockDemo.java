package org.hf.application.javabase.thread.lock;

import java.util.concurrent.locks.StampedLock;

/**
 * <p> StampedLock锁demo </p >
 * StampedLock是不可重入锁
 * StampedLock的使用有局限性
 *  1.对于读多写少的场景 StampedLock 性能很好，简单的应用场景基本上可以替代 ReadWriteLock
 *  2.StampedLock 在命名上并没有 Reentrant，StampedLock 是不可重入的！
 *  3.StampedLock 的悲观读锁、写锁都不支持条件变量（无法使用Condition: 意思就是
 *      如果乐观读锁stamp变量经validate()方法验证为false, 也就是读之前发生了写的操作, 那么这个时候要升级为悲观读锁,
 *      此时的条件变量stamp不可用,  写锁的条件变量stamp也是不可用的.）
 * 使用案例 {@link // org.hf.application.javabase.thread.state.StampedThread}
 *
 * @author HF
 * @date 2022-11-25
 **/
public class StampedLockDemo {

    @SuppressWarnings({"all"})
    public static void main(String[] args) {
//        stampedLockTest();
        /**
         * 这里会造成死锁
         * 对比 {@link org.hf.application.javabase.thread.lock.ReentrantLockDemo.parentSubReentrant}
         */
        SonLockObj lock = new SonLockObj();
        lock.f1();
    }

    /**
     * 测试StampedLock锁不可重入
     */
    private static void stampedLockTest() {
        StampedLock lock = new StampedLock();
        long stamp1 = lock.writeLock();
        System.out.println(1);
        // 这里获取不到锁了, 原因是上面已经获取过锁并且没有释放,所以这里获取不到了
        long stamp2 = lock.writeLock();
        System.out.println(2);
        lock.unlock(stamp2);
        lock.unlock(stamp1);
    }
}

/**
 * 父类
 */
class ParentLockObj {
    /**
     * 声明一个锁对象
     */
    final StampedLock lock = new StampedLock();

    public void f1(){
        long parentLock = lock.writeLock();
        System.out.println("f1 from parent");
        lock.unlock(parentLock);
    }
}

/**
 * 子类
 */
class SonLockObj extends ParentLockObj {

    @Override
    public void f1() {
        // 这里获取了锁
        long subLock = super.lock.writeLock();
        System.out.println("f1 lock");
        // 父类中再获取锁,而子类锁没有释放就获取不到锁,从而形成了死锁
        super.f1();
        System.out.println("f1 from son");
        lock.unlock(subLock);
    }
}
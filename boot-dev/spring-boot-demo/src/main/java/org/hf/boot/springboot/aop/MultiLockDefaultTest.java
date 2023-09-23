package org.hf.boot.springboot.aop;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.assertj.core.util.Lists;

import java.util.List;

/**
 * <p> 分布式锁测试类{@link PrefixRedisLockUtil} </p>
 *
 * @author hufei
 * @date 2023/4/16 15:37
*/
public class MultiLockDefaultTest {

    /**
     * 执行结果:
     * supplier-lock, lockName-lock1, requestId-123456, expiredInMilliSeconds-1, maxWaitTimeInMilliSeconds-0
     * supplier-lock, lockName-lock2, requestId-123456, expiredInMilliSeconds-1, maxWaitTimeInMilliSeconds-0
     * supplier-lock, lockName-lock3, requestId-123456, expiredInMilliSeconds-1, maxWaitTimeInMilliSeconds-0
     * 测试一下
     * test
     * supplier-unlock, lockName-lock3, requestId-123456, expiredInMilliSeconds-1, maxWaitTimeInMilliSeconds-0
     * test
     * supplier-unlock, lockName-lock2, requestId-123456, expiredInMilliSeconds-1, maxWaitTimeInMilliSeconds-0
     * test
     * supplier-unlock, lockName-lock1, requestId-123456, expiredInMilliSeconds-1, maxWaitTimeInMilliSeconds-0
     * test
     */
    public static void main(String[] args) {
        MultiLockDefaultTest test = new MultiLockDefaultTest();
        String s = test.multiLockDefault(new MultiLockDefaultTest()::test, Lists.newArrayList("lock1", "lock2", "lock3"), "123456", 1, 0);
        System.out.println(s);
    }

    public String test() {
        System.out.println("测试一下");
        return "test";
    }

    public <T> T multiLockDefault(ThrowableSupplier<T> supplier, List<String> lockNameList, String requestId, long expiredInMilliSeconds, long maxWaitTimeInMilliSeconds) {
        LockContext<T> lockContext = new LockContext<>();
        lockContext.setSupplier(supplier);
        lockContext.setLockNameList(lockNameList);
        lockContext.setLockNameIndex(lockNameList.size() - 1);
        lockContext.setRequestId(requestId);
        lockContext.setExpiredInMilliSeconds(expiredInMilliSeconds);
        lockContext.setMaxWaitTimeInMilliSeconds(maxWaitTimeInMilliSeconds);
        return multiLockLoop(lockContext);
    }

    private <T> T multiLockLoop(LockContext<T> lockContext) {
        if (lockContext.getLockNameIndex() > 0) {
            // 获取需要执行的业务对象实例的引用
            ThrowableSupplier<T> newTask = () -> lock(lockContext);
            LockContext<T> thisContext = lockContext.copy();
            thisContext.setSupplier(newTask);
            thisContext.decreaseIndex();
            return this.multiLockLoop(thisContext);
        }
        return lock(lockContext);
    }

    public <T> T lock(LockContext<T> lockContext) {
        return this.lock(lockContext.getSupplier(), lockContext.getLockNameList().get(lockContext.getLockNameIndex()), lockContext.getRequestId(), lockContext.getExpiredInMilliSeconds(), lockContext.getMaxWaitTimeInMilliSeconds());
    }

    @SneakyThrows
    public <T> T lock(ThrowableSupplier<T> supplier, String lockName, String requestId, long expiredInMilliSeconds, long maxWaitTimeInMilliSeconds) {
        try {
            System.out.println("supplier-lock" + ", lockName-" + lockName + ", requestId-" + requestId + ", expiredInMilliSeconds-" + expiredInMilliSeconds + ", maxWaitTimeInMilliSeconds-" + maxWaitTimeInMilliSeconds);
            T t = supplier.get();
            System.out.println(t);
            return t;
        } catch (Exception exception) {
            throw new RuntimeException("");
        } finally {
            System.out.println("supplier-unlock" + ", lockName-" + lockName + ", requestId-" + requestId + ", expiredInMilliSeconds-" + expiredInMilliSeconds + ", maxWaitTimeInMilliSeconds-" + maxWaitTimeInMilliSeconds);
        }
    }

    public void count(int total) {
        if (total > 0) {
            count(total - 1);
        }
        System.out.println(total);
    }

    /**
     * 当前锁上下文对象
     * @param <T> 参数类型声明
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class LockContext<T> {
        /**
         * 需要执行业务逻辑对象
         */
        ThrowableSupplier<T> supplier;
        /**
         * 多个锁的名称集合
         */
        List<String> lockNameList;
        /**
         * 锁名称索引
         */
        int lockNameIndex;
        /**
         * 请求唯一标识
         */
        String requestId;
        /**
         * 获取锁的失效时间
         */
        long expiredInMilliSeconds;
        /**
         * 获取锁等待时间
         */
        long maxWaitTimeInMilliSeconds;

        void decreaseIndex() {
            lockNameIndex--;
        }

        LockContext<T> copy() {
            LockContext<T> newOne = new LockContext<>();
            newOne.setSupplier(supplier);
            newOne.setLockNameList(lockNameList);
            newOne.setLockNameIndex(lockNameIndex);
            newOne.setRequestId(requestId);
            newOne.setExpiredInMilliSeconds(expiredInMilliSeconds);
            newOne.setMaxWaitTimeInMilliSeconds(maxWaitTimeInMilliSeconds);
            return newOne;
        }
    }
}
package org.hf.boot.springboot.aop;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.UUID;

/**
 * <p> redis锁工具 </p >
 * 3.redis锁注解工具类
 * @author hufei
 * @date 2023-04-11
 **/
@Component
@Slf4j
public class PrefixRedisLockUtil {

    @Autowired
    private RedisDistributedLock lock;

    /**
     * 默认的锁失效时间, 10秒
     */
    private static final long DEFAULT_LOCK_EXPIRED_MILLI_SECONDS = 10 * 1000L;

    /**
     * 获取锁方法
     * @param supplier                     通过函数式接口的方式接收需要执行的业务逻辑
     * @param lockName                     锁名称
     * @param requestId                    请求唯一标识
     * @param expiredInMilliSeconds        锁失效时间
     * @param maxWaitTimeInMilliSeconds    获取锁等待时间
     * @param <T>                          参数类型声明
     * @return  T 返回结果
     */
    @SneakyThrows
    public <T> T lock(ThrowableSupplier<T> supplier, String lockName, String requestId, long expiredInMilliSeconds, long maxWaitTimeInMilliSeconds) {
        boolean locked = false;
        try {
            log.debug("Redis分布式加锁, lockName:{}, requestId:{}, expiredInMilliSeconds:{}, maxWaitTimeInMilliSeconds:{}", lockName, requestId, expiredInMilliSeconds, maxWaitTimeInMilliSeconds);
            if (maxWaitTimeInMilliSeconds > 0) {
                locked = lock.tryLock(lockName, requestId, expiredInMilliSeconds, maxWaitTimeInMilliSeconds);
            } else {
                locked = lock.tryLock(lockName, requestId, expiredInMilliSeconds);
            }
            if (!locked) {
                log.debug("无法获取到分布式锁: lockName:{}, requestId:{}", lockName, requestId);
                throw new RuntimeException("系统繁忙，请重试！");
            }
            log.debug("成功获取到分布式锁, 开始执行业务逻辑: lockName:{}, requestId:{}", lockName, requestId);
            T res = supplier.get();
            log.debug("执行业务逻辑结束: lockName:{}, requestId:{}", lockName, requestId);
            return res;
        } catch (Throwable e) {
            log.warn("分布式锁锁定区域内，执行业务逻辑发生错误", e);
            throw e;
        } finally {
            if (locked) {
                log.debug("成功释放分布式锁: lockName:{}, requestId:{}", lockName, requestId);
                lock.unLock(lockName, requestId);
            }
        }
    }

    public <T> T lock(ThrowableSupplier<T> supplier, String lockName, String requestId) {
        return this.lock(supplier, lockName, requestId, DEFAULT_LOCK_EXPIRED_MILLI_SECONDS, 0);
    }

    public <T> T lock(ThrowableSupplier<T> supplier, String lockName, long expiredInMilliSeconds, long maxWaitTimeInMilliSeconds) {
        String requestId = UUID.randomUUID().toString();
        return this.lock(supplier, lockName, requestId, expiredInMilliSeconds, maxWaitTimeInMilliSeconds);
    }

    public <T> T lock(ThrowableSupplier<T> supplier, String lockName, long expiredInMilliSeconds) {
        return this.lock(supplier, lockName, expiredInMilliSeconds, 0);
    }

    public <T> T lock(ThrowableSupplier<T> supplier, String lockName) {
        return this.lock(supplier, lockName, DEFAULT_LOCK_EXPIRED_MILLI_SECONDS);
    }

    public <T> T lock(LockContext<T> lockContext) {
        return this.lock(lockContext.getSupplier(), lockContext.getLockNameList().get(lockContext.getLockNameIndex()), lockContext.getRequestId(), lockContext.getExpiredInMilliSeconds(), lockContext.getMaxWaitTimeInMilliSeconds());
    }

    /**
     * 获取锁
     * @param supplier                  通过函数式接口的方式接收需要执行的业务逻辑
     * @param lockName                  锁名称
     * @param requestId                 请求唯一标识
     * @param expiredInMilliSeconds     锁失效时间
     * @param maxWaitTimeInMilliSeconds 获取锁等待时间
     * @param <T>                       参数类型声明
     * @return T 返回结果
     */
    public <T> T lockDefault(ThrowableSupplier<T> supplier, String lockName, String requestId, long expiredInMilliSeconds, long maxWaitTimeInMilliSeconds) {
        if (StringUtils.isBlank(requestId)) {
            requestId = UUID.randomUUID().toString();
        }
        expiredInMilliSeconds = expiredInMilliSeconds > 0 ? expiredInMilliSeconds : DEFAULT_LOCK_EXPIRED_MILLI_SECONDS;
        return this.lock(supplier, lockName, requestId, expiredInMilliSeconds, maxWaitTimeInMilliSeconds);
    }

    /**
     * 组装多重锁的上下文内容
     * @param supplier                      通过函数式接口的方式接收需要执行的业务逻辑
     * @param lockNameList                  锁名称集合
     * @param requestId                     请求唯一标识
     * @param expiredInMilliSeconds         锁失效时间
     * @param maxWaitTimeInMilliSeconds     获取锁等待时间
     * @param <T>                           参数类型声明
     * @return T 返回结果
     */
    public <T> T multiLockDefault(ThrowableSupplier<T> supplier, List<String> lockNameList, String requestId, long expiredInMilliSeconds, long maxWaitTimeInMilliSeconds) {
        if (CollectionUtils.isEmpty(lockNameList)) {
            throw new RuntimeException("lockNameList is empty!");
        }
        if (StringUtils.isBlank(requestId)) {
            requestId = UUID.randomUUID().toString();
        }
        LockContext<T> lockContext = new LockContext<>();
        lockContext.setSupplier(supplier);
        lockContext.setLockNameList(lockNameList);
        lockContext.setLockNameIndex(lockNameList.size() - 1);
        lockContext.setRequestId(requestId);
        lockContext.setExpiredInMilliSeconds(expiredInMilliSeconds);
        lockContext.setMaxWaitTimeInMilliSeconds(maxWaitTimeInMilliSeconds);
        return multiLockLoop(lockContext);
    }

    /**
     * 循环组装每个锁的对象内容, 并按照锁名称顺序进行加锁 -> 执行业务 -> 释放锁
     * @param lockContext 多重锁内容对象
     * @param <T> 参数类型声明
     * @return T 返回执行结果
     */
    private <T> T multiLockLoop(LockContext<T> lockContext) {
        if (lockContext.getLockNameIndex() > 0) {
            // 获取需要执行的业务对象实例的引用, 这里创建新的引用的时候会将前一次的引用包含在里面, 这里的操作就会在调用最底层lock()方法的时候会一层层再去执行包含在里层引用对象的方法调用
            ThrowableSupplier<T> newTask = () -> lock(lockContext);
            LockContext<T> thisContext = lockContext.copy();
            // 设置下次调用的时候需要执行的业务逻辑
            thisContext.setSupplier(newTask);
            thisContext.decreaseIndex();
            return this.multiLockLoop(thisContext);
        }
        /*
         * 这里执行的步骤:当LockNameIndex=0的时候开始调用lock方法,然后是LockNameIndex=1 -> LockNameIndex=2(到这里时也是获取锁的顺序),在调用最底层lock方法的逻辑时,因为LockNameIndex=0
         * 的supplier业务逻辑实例包含了LockNameIndex=1和LockNameIndex=2的实例引用,所以实例业务逻辑的执行逻辑顺序是LockNameIndex=2 -> LockNameIndex=1 -> LockNameIndex=0(到这里时
         * 也是释放锁的顺序),而执行LockNameIndex=2的实例引用逻辑时,就会调用初始传入的supplier引用调用业务逻辑,并获取执行结果,而后LockNameIndex=1 -> LockNameIndex=0再执行lock方法时就
         * 会获取已经返回的执行结果,综前所述的结论是:获取锁的顺序是LockNameIndex=0 -> LockNameIndex=1 -> LockNameIndex=2, 然后执行最初始传入的supplier业务逻辑并返回结果, 这里多次调用
         * lock()方法的supplier.get()逻辑会获取已经返回的结果,调用顺序:LockNameIndex=2 -> LockNameIndex=1 -> LockNameIndex=0(这也是释放锁的顺序),最后多次结果的返回是同一个结果,最外
         * 层调用获取的是LockNameIndex=0调用lock方法返回的最终结果
         */
        return this.lock(lockContext);
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
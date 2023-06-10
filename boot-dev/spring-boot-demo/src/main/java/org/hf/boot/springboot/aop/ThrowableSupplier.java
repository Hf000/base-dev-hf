package org.hf.boot.springboot.aop;

/**
 * <p> 函数接口 </p >
 * 自定义redis锁CustomPrefixRedisLock实现 - 5
 * @author hufei
 * @date 2023-04-13
 **/
public interface ThrowableSupplier<T> {

    /**
     * 函数方法
     * @return T
     * @throws Throwable 异常
     */
    T get() throws Throwable;
}
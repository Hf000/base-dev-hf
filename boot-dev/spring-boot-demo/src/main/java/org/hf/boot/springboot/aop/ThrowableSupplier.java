package org.hf.boot.springboot.aop;

/**
 * <p> 函数接口 </p >
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
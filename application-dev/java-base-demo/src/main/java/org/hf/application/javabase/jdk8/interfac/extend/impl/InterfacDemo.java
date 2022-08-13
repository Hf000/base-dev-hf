package org.hf.application.javabase.jdk8.interfac.extend.impl;

/**
 * <p> 接口 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2022/8/13 20:41
 */
public interface InterfacDemo {

    default void method() {
        System.out.println("InterfacDemo的method");
    }
}

package org.hf.application.javabase.jdk8.interfac.multiple.impl;

/**
 * <p> 接口 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2022/8/13 20:46
 */
public interface MultipleInterfacDemoTwo {

    default void method() {
        System.out.println("MultipleInterfacDemoTwo的method");
    }
}

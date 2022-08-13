package org.hf.application.javabase.jdk8.interfac.defaul;

/**
 * <p> Jdk8特性: 接口静态方法 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2022/8/13 20:37
 */
public interface DefaultMethodInterfac {

    /**
     * jdk8特性, 默认方法, 需要接口的实例化对象调用
     * 应用场景: 一般公共且不依赖其他的业务可以用默认方法实现
     */
    default void method() {
        System.out.println("DefaultMethodInterfac的method");
    }

}

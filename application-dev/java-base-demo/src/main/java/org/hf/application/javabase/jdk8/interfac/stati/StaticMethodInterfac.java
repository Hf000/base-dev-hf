package org.hf.application.javabase.jdk8.interfac.stati;

/**
 * <p> Jdk8特性: 接口静态方法 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2022/8/13 20:51
 */
public interface StaticMethodInterfac {

    /**
     * jdk8特性, 静态方法, 不需要接口实例化对象调用,可以直接接口名.方法名调用
     */
    static void method() {
        System.out.println("StaticMethodInterfac的method");
    }

}

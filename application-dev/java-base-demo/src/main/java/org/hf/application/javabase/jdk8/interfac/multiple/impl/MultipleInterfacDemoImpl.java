package org.hf.application.javabase.jdk8.interfac.multiple.impl;

/**
 * <p> Jdk8实现多接口同名默认方法测试 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2022/8/13 20:47
 */
public class MultipleInterfacDemoImpl implements MultipleInterfacDemo, MultipleInterfacDemoTwo {

    public static void main(String[] args) {
        MultipleInterfacDemoImpl impl = new MultipleInterfacDemoImpl();
        // Jdk8实现多接口, 且接口具有同名方法, 那么实现类必须重写同名方法, 调用时调用重写后的方法
        impl.method();
    }

    @Override
    public void method() {
        System.out.println("MultipleInterfacDemoImpl的method");
    }
}

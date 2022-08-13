package org.hf.application.javabase.jdk8.interfac.extend.impl;

/**
 * <p> Jdk8接口特性测试 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2022/8/13 20:41
 */
public class InterfacDemoImpl extends InterfacDemoImplParent implements InterfacDemo  {

    public static void main(String[] args) {
        InterfacDemoImpl impl = new InterfacDemoImpl();
        // 这里调用的不是接口的默认方法而是父类的同名方法,Java遵循类优先原则
        impl.method();
    }

}

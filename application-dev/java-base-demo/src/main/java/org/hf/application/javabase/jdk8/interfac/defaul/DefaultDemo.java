package org.hf.application.javabase.jdk8.interfac.defaul;

/**
 * <p> Jdk8默认方法测试 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2022/8/13 20:36
 */
public class DefaultDemo implements DefaultMethodInterfac {

    public static void main(String[] args) {
        // 接口实例化对象调用默认方法
        DefaultDemo defaultDemo = new DefaultDemo();
        defaultDemo.method();
    }

}

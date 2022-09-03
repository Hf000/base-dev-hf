package org.hf.application.javabase.basic.extend;

/**
 * <p> 继承Demo </p>
 * java三大特性:封装、继承、多态
 * @author hufei
 * @version 1.0.0
 * @date 2022/9/3 15:24
 */
public class ExtendDemo {

    public static void main(String[] args) {
        Son son = new Son();
        // 子类调用父类非私有方法
        son.method();
        // 子类重写父类非私有方法调用
        son.methodTwo();
    }

}

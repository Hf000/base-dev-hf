package org.hf.application.javabase.basic.abstrac;

import org.checkerframework.checker.units.qual.A;

/**
 * <p> 抽象demo </p>
 * 抽象类不需要一定有抽象方法,但是有抽象方法的类一定是抽象类,抽象类中有可以有具体的实现
 * 抽象主要用来抽象类别,重构的结果:抽象类是对某类事物的抽象,约束一类事物的特性,例如喜鹊和啄木鸟都是鸟类
 * 接口主要用来抽象功能,设计的结果:接口主要局部行为的抽象,约束某个具体的功能,例如鸟和飞机都具有飞行的能力
 * @author hufei
 * @version 1.0.0
 * @date 2022/9/3 14:33
 */
public class AbstractDemo {

    public static void main(String[] args) {
        // 抽象类不能实例化,直接new AbstractImpl()会报错;可以通过匿名内部类的方式实现,相当于创建了一个实例化对象
        AbstractImpl abstractInstance = new AbstractImpl() {
            @Override
            void abstractMethod() {
                System.out.println("匿名内部类方式实现抽象类的抽象方法");
            }
        };
        // 抽象类的普通方法调用
        abstractInstance.method();
        // 抽象类的抽象方法调用
        abstractInstance.abstractMethod();
        // 抽象类的静态方法调用
        AbstractImpl.staticMethod();
    }

}

abstract class AbstractImpl {
    public void method() {
        System.out.println("抽象类的普通方法");
    }
    public static void staticMethod() {
        System.out.println("抽象类的静态方法");
    }
    /**
     * 抽象类的抽象方法
     */
    abstract void abstractMethod();
}

class AbstractSon extends AbstractImpl{
    @Override
    void abstractMethod() {
        System.out.println("子类继承父类是抽象类,必须重写抽象方法");
    }
}
package org.hf.application.javabase.basic.clazz;

/**
 * <p> 类的demo </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2022/9/3 15:12
 */
public class ClassDemo {

    public static void main(String[] args) {
        // 外部调用静态匿名内部类方法
        new ClassDemo.StaticInnerClassDemo().staticInnerMethod();
        // 内部调用静态匿名内部类方法
        new StaticInnerClassDemo().staticInnerMethod();
        //调用非静态内部类方法
        new ClassDemo().new InnerClassDemo().innerMethod();
        // 调用外部类方法
        new OuterClassDemo().outerClassMethod();
    }
    /**
     * 需要加public修饰类才能跨包调用,否则不可以跨包调用
     */
    static class StaticInnerClassDemo{
        public void staticInnerMethod() {
            System.out.println("静态内部类的方法");
        }
    }
    /**
     * 加上public后可以跨包调用
     */
    public class InnerClassDemo{
        public void innerMethod() {
            System.out.println("匿名内部类方法");
        }
    }
}

/**
 * 此类不可以跨包使用,不能被public修饰
 */
class OuterClassDemo {
    public void outerClassMethod() {
        System.out.println("外部类方法");
    }
}

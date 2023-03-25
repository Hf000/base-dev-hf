package org.hf.application.javabase.basic.extend;

/**
 * <p> 静态代码块和构造执行顺序 </p>
 * 执行顺序: 1.父类的静态代码块 -> 子类静态代码块 -> 父类非静态代码块(匿名构造块) -> 父类构造方法 -> 子类非静态代码块(匿名构造块) -> 子类构造方法
 *          2. 匿名构造块 -> 构造方法
 * 静态代码只会执行一次
 *
 * @author hufei
 * @version 1.0.0
 * @date 2023/3/25 21:53
 */
public class StaticDemo {

    /**
     * 执行结果: ParentDemo 1-ParentDemo-ParentDemo static-ParentDemo 1-ParentDemo-SonDemo-
     * 解析: 执行main方法, 先加载ParentDemo和SonDemo类, 所以会先执行ParentDemo的静态代码new ParentDemo()(这里匿名构造会先执行,后执行构造方法)和static{},
     * 然后实例化SonDemo,会先执行父类的构造在执行子类的构造
     */
    public static void main(String[] args) {
        SonDemo son = new SonDemo();
    }
}

class ParentDemo {

    private static final ParentDemo pDemo = new ParentDemo();

    static {
        System.out.print("ParentDemo static-");
    }

    public ParentDemo() {
        System.out.print("ParentDemo-");
    }

    // 匿名构造块
    {
        System.out.print("ParentDemo 1-");
    }
}

class SonDemo extends ParentDemo {

    public SonDemo() {
        System.out.print("SonDemo-");
    }

    /**
     * 执行结果: ParentDemo 1-ParentDemo-ParentDemo static-main-ParentDemo 1-ParentDemo-SonDemo-
     * 执行main方法会先加载ParentDemo和SonDemo类,此时SonDemo类的父类ParentDemo会执行静态代码所以输出ParentDemo 1-ParentDemo-ParentDemo static-, 然后输出main-
     * , 然后实例化SonDemo对象时先执行父类的构造,所以输出ParentDemo 1-ParentDemo-, 最后执行子类构造输出SonDemo-
     */
    public static void main(String[] args) {
        System.out.print("main-");
        SonDemo son = new SonDemo();
    }
}
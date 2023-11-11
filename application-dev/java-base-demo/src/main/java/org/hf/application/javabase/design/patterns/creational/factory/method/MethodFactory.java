package org.hf.application.javabase.design.patterns.creational.factory.method;

/**
 * <p> 工厂模式-工厂方法 </p>
 * 每次增加一个独立产品对象, 就得新建一个工产来实现
 * 和抽象工厂的区别: 工厂方法模式提供的所有产品都是衍生自同一个接口或抽象类，而抽象工厂模式所提供的产品则是衍生自不同的接口或抽象类。
 * 抽象工厂: 创建的是一类的对象, 可以理解为一个品牌里面的不同产品
 * 工厂方法: 创建的是某一个对象, 没有区分一类的概念
 *
 * @author hufei
 * @version 1.0.0
 * @date 2022/4/23 15:10
 */
public class MethodFactory {

    /**
     * 创建对应的工厂去创建对应的对象执行对应的方法
     */
    public static void createLenovoInstance() {
        IMethodFactory methodFactory = new LenovoMethodFactoryImpl();
        methodFactory.createKeyboard().input();
    }

    /**
     * 创建对应的工厂去创建对应的对象执行对应的方法
     */
    public static void createThinkPadInstance() {
        IMethodFactory methodFactory = new ThinkPadMethodFactoryImpl();
        methodFactory.createKeyboard().input();
    }

}

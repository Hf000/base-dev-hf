package org.hf.application.javabase.design.patterns.creational.factory.abstractf;

/**
 * <p> 工厂模式-抽象工厂 </p>
 * 和工厂方法的区别:
 *      抽象工厂: 创建的是一类的对象, 可以理解为一个品牌里面的不同产品
 *      工厂方法: 创建的是某一个对象, 没有区分一类的概念
 * @author hufei
 * @version 1.0.0
 * @date 2022/4/23 15:38
 */
public class AbstractFactory {

    /**
     * 创建某个工厂实例创建某个对象执行方法
     */
    public static void createLenovoInstance() {
        IAbstractFactory abstractFactory = new LenovoAbstractFactoryImpl();
        abstractFactory.createKeyboard().input();
        abstractFactory.createMonitor().output();
    }

    /**
     * 创建某个工厂实例创建某个对象执行方法
     */
    public static void createThinkPadInstance() {
        IAbstractFactory abstractFactory = new ThinkPadAbstractFactoryImpl();
        abstractFactory.createKeyboard().input();
        abstractFactory.createMonitor().output();
    }
}

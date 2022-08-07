package org.hf.application.javabase.design.patterns;

import lombok.extern.slf4j.Slf4j;
import org.hf.application.javabase.design.patterns.creational.builder.classic.ClassicBuilder;
import org.hf.application.javabase.design.patterns.creational.builder.simple.SimpleBuilder;
import org.hf.application.javabase.design.patterns.creational.factory.abstractf.AbstractFactory;
import org.hf.application.javabase.design.patterns.creational.factory.method.MethodFactory;
import org.hf.application.javabase.design.patterns.creational.factory.simple.SimpleFactory;
import org.hf.application.javabase.design.patterns.creational.prototype.Prototype;
import org.hf.application.javabase.design.patterns.creational.singleton.SingleEnumModel;
import org.hf.application.javabase.design.patterns.creational.singleton.SingleModel;
import org.hf.application.javabase.design.patterns.creational.singleton.SingleStaticModel;

/**
 * <p> 设计模式测试类 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2021/11/15 21:57
 */
@Slf4j
public class DesignPatternsTest {

    public static void main(String[] args) {

        testPrototypePattern();
        //testBuilderPattern();
        //testFactoryPattern();
        // testSingletonPattern();
    }

    private static void testPrototypePattern() {
        // 原型模式
        Prototype prototype = new Prototype();
        prototype.getPrototype();
    }

    public static void testBuilderPattern() {
        // 简单建造者
        SimpleBuilder.createInstance();
        // 经典建造者
        ClassicBuilder.createInstance();
    }

    /**
     * 工厂方法
     */
    public static void testFactoryPattern(){
        // 建单工厂
        SimpleFactory.createInstance("ThinkPad").input();
        SimpleFactory.createInstance("Lenovo").input();
        // 工厂方法
        MethodFactory.createLenovoInstance();
        MethodFactory.createThinkPadInstance();
        // 抽象工厂
        AbstractFactory.createLenovoInstance();
        AbstractFactory.createThinkPadInstance();
    }

    /**
     * 单例模式测试
     */
    public static void testSingletonPattern() {

        SingleModel singleModel1 = SingleModel.getInstance();
        SingleModel singleModel2 = SingleModel.getInstance();
        log.info("多重判加锁空单例测试===>{}", singleModel1 == singleModel2);

        SingleStaticModel singleStaticModel1 = SingleStaticModel.getInstance();
        SingleStaticModel singleStaticModel2 = SingleStaticModel.getInstance();
        log.info("静态内部类单例测试===>{}", singleStaticModel1 == singleStaticModel2);

        SingleEnumModel singleEnumModel1 = SingleEnumModel.getInstance();
        SingleEnumModel singleEnumModel2 = SingleEnumModel.getInstance();
        log.info("内部枚举类单例测试===>{}", singleEnumModel1 == singleEnumModel2);
    }

}

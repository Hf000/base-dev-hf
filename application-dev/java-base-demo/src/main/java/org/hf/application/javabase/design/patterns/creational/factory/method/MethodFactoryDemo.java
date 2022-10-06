package org.hf.application.javabase.design.patterns.creational.factory.method;

/**
 * <p> 工厂模式-工厂方法 </p>
 * 工厂方法: 定义一个创建对象的接口, 让子类决定实例化那个类
 *
 * @author hufei
 * @version 1.0.0
 * @date 2022/4/23 15:10
 */
public class MethodFactoryDemo {

    public static void main(String[] args) {
        // 工厂方法
        MethodFactory.createLenovoInstance();
        MethodFactory.createThinkPadInstance();
    }
}

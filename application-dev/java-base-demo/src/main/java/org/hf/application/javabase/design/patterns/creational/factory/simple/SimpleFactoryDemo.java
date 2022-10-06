package org.hf.application.javabase.design.patterns.creational.factory.simple;

import org.apache.commons.lang3.StringUtils;

/**
 * <p> 工厂模式-建单工厂 </p>
 * 简单工厂: 一个工厂类根据传入的参量决定创建出那一种产品类的实例
 *
 * @author hufei
 * @version 1.0.0
 * @date 2022/4/23 14:57
 */
public class SimpleFactoryDemo {

    public static void main(String[] args) {
        // 建单工厂
        SimpleFactory.createInstance("ThinkPad").input();
        SimpleFactory.createInstance("Lenovo").input();
    }
}

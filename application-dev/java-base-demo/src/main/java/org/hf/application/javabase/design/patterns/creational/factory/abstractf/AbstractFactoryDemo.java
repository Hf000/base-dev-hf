package org.hf.application.javabase.design.patterns.creational.factory.abstractf;

/**
 * <p> 抽象工厂demo </p>
 * 抽象工厂: 创建相关或依赖对象的家族, 而无需明确指定具体类
 *
 * @author hufei
 * @version 1.0.0
 * @date 2022/4/23 15:38
 */
public class AbstractFactoryDemo {

    public static void main(String[] args) {
        // 抽象工厂
        AbstractFactory.createLenovoInstance();
        AbstractFactory.createThinkPadInstance();
    }
}

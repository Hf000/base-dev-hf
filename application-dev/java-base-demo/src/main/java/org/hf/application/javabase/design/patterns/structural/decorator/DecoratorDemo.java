package org.hf.application.javabase.design.patterns.structural.decorator;

/**
 * <p> 装饰者模式 </p>
 * 装饰者模式: 动态的给对象添加新的功能
 *
 * @author hufei
 * @date 2022/10/6 11:07
*/
public class DecoratorDemo {

    public static void main(String[] args) {
        //被扩展的对象
        Tea iceTea = new IceTea();
        //创建扩展对象
        DecoratorTea peachTea = new PeachTea();
        peachTea.setTea(iceTea);
        //调用
        peachTea.making();
        //创建扩展对象
        DecoratorTea cocoTea = new CocoTea();
        cocoTea.setTea(peachTea);
        cocoTea.making();
    }
}

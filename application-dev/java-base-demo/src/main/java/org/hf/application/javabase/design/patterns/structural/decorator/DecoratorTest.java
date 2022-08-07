package org.hf.application.javabase.design.patterns.structural.decorator;


public class DecoratorTest {

    public static void main(String[] args) {
        //被扩展的对象
        Tea iceTea = new IceTea();

        //创建扩展对象
        DecoratorTea peachTea = new PeachTea();
        peachTea.setTea(iceTea);
        //调用
        //peachTea.making();

        //创建扩展对象
        DecoratorTea cocoTea = new CocoTea();
        cocoTea.setTea(peachTea);
        cocoTea.making();
    }
}

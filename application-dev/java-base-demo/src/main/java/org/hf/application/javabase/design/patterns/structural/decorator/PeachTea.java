package org.hf.application.javabase.design.patterns.structural.decorator;


public class PeachTea extends DecoratorTea {


    //重写making方法

    @Override
    public void making() {
        //调用父类中的making方法
        super.making();

        //增强[功能扩展]
        addPeach();
    }

    //扩展功能
    public void addPeach(){
        System.out.println("添加桃子！");
    }
}

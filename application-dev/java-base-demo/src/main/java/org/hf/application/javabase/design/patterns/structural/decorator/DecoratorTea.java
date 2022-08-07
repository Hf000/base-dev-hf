package org.hf.application.javabase.design.patterns.structural.decorator;


public abstract class DecoratorTea implements Tea{

    //被增强的对象
    private Tea tea;
    //注入被增强的对象
    public void setTea(Tea tea) {
        this.tea = tea;
    }

    @Override
    public void making() {
        //调用被增强对象
        tea.making();
    }






}

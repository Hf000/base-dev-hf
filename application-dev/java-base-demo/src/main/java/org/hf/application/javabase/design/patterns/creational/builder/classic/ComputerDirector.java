package org.hf.application.javabase.design.patterns.creational.builder.classic;

/**
 * 组装对象按哪种方式生成对象的知道类
 * @author HF
 */
public class ComputerDirector {
    public void makeComputer(ComputerBuilder builder){
        // 这里可以根据需求按规则实现一些业务逻辑
        builder.setUsbCount();
        builder.setDisplay();
        builder.setKeyboard();
    }
}
package org.hf.application.javabase.design.patterns.creational.builder.classic;

/**
 * 抽象构建者对象
 */
public abstract class ComputerBuilder {
    public abstract void setUsbCount();
    public abstract void setKeyboard();
    public abstract void setDisplay();

    public abstract Computer getComputer();
}
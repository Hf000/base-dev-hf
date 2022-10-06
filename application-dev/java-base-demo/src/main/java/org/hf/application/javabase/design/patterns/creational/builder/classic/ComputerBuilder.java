package org.hf.application.javabase.design.patterns.creational.builder.classic;

/**
 * 抽象构建者对象
 * @author HF
 */
public abstract class ComputerBuilder {
    /**
     * 抽象方法
     */
    public abstract void setUsbCount();
    /**
     * 抽象方法
     */
    public abstract void setKeyboard();
    /**
     * 抽象方法
     */
    public abstract void setDisplay();
    /**
     * 抽象方法
     * @return Computer
     */
    public abstract Computer getComputer();
}
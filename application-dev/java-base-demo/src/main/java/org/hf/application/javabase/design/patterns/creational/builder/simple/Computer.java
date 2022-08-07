package org.hf.application.javabase.design.patterns.creational.builder.simple;

import lombok.ToString;

/**
 * 自定义实体bean
 */
@ToString
public class Computer {
    private final String cpu;//必须
    private final String ram;//必须
    private final int usbCount;//可选
    private final String keyboard;//可选
    private final String display;//可选

    public Computer(ComputerSimpleBuilder computerSimpleBuilder){
        this.cpu = computerSimpleBuilder.getCpu();
        this.ram = computerSimpleBuilder.getRam();
        this.keyboard = computerSimpleBuilder.getKeyboard();
        this.usbCount = computerSimpleBuilder.getUsbCount();
        this.display = computerSimpleBuilder.getDisplay();
    }

    public String getCpu() {
        return cpu;
    }

    public String getRam() {
        return ram;
    }

    public int getUsbCount() {
        return usbCount;
    }

    public String getKeyboard() {
        return keyboard;
    }

    public String getDisplay() {
        return display;
    }
}
package org.hf.application.javabase.design.patterns.creational.builder.simple;

import lombok.ToString;

/**
 * 自定义实体bean
 * @author HF
 */
@ToString
public class Computer {
    private final String cpu;
    private final String ram;
    private final int usbCount;
    private final String keyboard;
    private final String display;

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
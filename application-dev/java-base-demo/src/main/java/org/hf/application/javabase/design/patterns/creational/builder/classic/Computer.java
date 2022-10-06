package org.hf.application.javabase.design.patterns.creational.builder.classic;

import lombok.Getter;
import lombok.ToString;

/**
 * 自定义实体bean
 */
@ToString
@Getter
public class Computer {
    private final String cpu;
    private final String ram;
    private int usbCount;
    private String keyboard;
    private String display;

    public Computer(String cup,String ram){
        this.cpu=cup;
        this.ram=ram;
    }

    public void setUsbCount(int usbCount) {
        this.usbCount = usbCount;
    }

    public void setKeyboard(String keyboard) {
        this.keyboard = keyboard;
    }

    public void setDisplay(String display) {
        this.display = display;
    }
}
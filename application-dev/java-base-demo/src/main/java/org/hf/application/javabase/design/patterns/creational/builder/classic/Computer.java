package org.hf.application.javabase.design.patterns.creational.builder.classic;

import lombok.Getter;
import lombok.ToString;

/**
 * 自定义实体bean
 */
@ToString
@Getter
public class Computer {
    private final String cpu;//必须
    private final String ram;//必须
    private int usbCount;//可选
    private String keyboard;//可选
    private String display;//可选

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
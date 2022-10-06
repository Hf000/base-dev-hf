package org.hf.application.javabase.design.patterns.creational.builder.simple;

/**
 * <p> 简单建造者类 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2022/4/23 16:34
 */
public class ComputerSimpleBuilder {

    private final String cpu;
    private final String ram;
    private int usbCount;
    private String keyboard;
    private String display;

    public ComputerSimpleBuilder(String cup,String ram){
        this.cpu=cup;
        this.ram=ram;
    }

    public ComputerSimpleBuilder setUsbCount(int usbCount) {
        this.usbCount = usbCount;
        return this;
    }
    public ComputerSimpleBuilder setKeyboard(String keyboard) {
        this.keyboard = keyboard;
        return this;
    }
    public ComputerSimpleBuilder setDisplay(String display) {
        this.display = display;
        return this;
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

    public Computer build(){
        return new Computer(this);
    }
}

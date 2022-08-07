package org.hf.application.javabase.design.patterns.structural.flyweight;

/**
* <p> 享元模式 </p>
* @author hufei
* @date 2022/7/13 21:22
*/
public class UnsharedConcreteFlyweight extends Flyweight {

    //接受外部状态
    public UnsharedConcreteFlyweight(String extrinsic) {
        super(extrinsic);
    }

    //根据外部状态进行逻辑处理
    @Override
    public void operate(int extrinsic) {
        System.out.println("不共享的具体Flyweight:"+extrinsic);
    }
}

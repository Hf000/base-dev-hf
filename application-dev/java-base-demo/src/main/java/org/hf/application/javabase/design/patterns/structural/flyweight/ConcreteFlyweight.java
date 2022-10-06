package org.hf.application.javabase.design.patterns.structural.flyweight;

/**
 * <p> 享元对象方法实现 </p>
 * @author hufei
 * @date 2022/10/6 12:24
*/
public class ConcreteFlyweight extends Flyweight {

    /**
     * 接受外部状态
     * @param extrinsic 入参
     */
    public ConcreteFlyweight(String extrinsic) {
        super(extrinsic);
    }

    /**
     * 根据外部状态进行逻辑处理
     * @param extrinsic 入参
     */
    @Override
    public void operate(int extrinsic) {
        System.out.println("具体Flyweight:"+extrinsic);
    }
}

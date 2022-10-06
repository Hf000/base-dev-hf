package org.hf.application.javabase.design.patterns.structural.decorator;

/**
 * <p> 需要被装饰的对象 </p>
 * @author hufei
 * @date 2022/10/6 11:13
*/
public class IceTea implements Tea {

    /**
     * 最原始的方法，需要对它进行扩展
     */
    @Override
    public void making() {
        System.out.println("制作一杯冰冻的水！");
    }
}

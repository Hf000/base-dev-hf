package org.hf.application.javabase.design.patterns.structural.adapter;

/**
 * <p> 适配器模式demo </p>
 * 适配器模式: 将一个类的方法接口转换成客户希望的另外一个接口
 *
 * @author hufei
 * @version 1.0.0
 * @date 2022/10/6 11:18
 */
public class AdapterDemo {

    public static void main(String[] args) {
        AdapterImpl adapter = new AdapterImpl(new FunctionInterfaceImpl());
        adapter.method("适配器测试", 1);
    }
}

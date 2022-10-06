package org.hf.application.javabase.design.patterns.structural.adapter;

/**
 * <p>  </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2022/10/6 11:20
 */
public class FunctionInterfaceImpl implements FunctionInterface {

    @Override
    public void method(String param, Object... objects) {
        if (objects != null) {
            for (Object object : objects) {
                System.out.println("object:" + object.toString());
            }
        }
        System.out.println("功能调用实现, param -> " + param);
    }
}

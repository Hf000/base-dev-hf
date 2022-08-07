package org.hf.application.javabase.design.patterns.creational.factory.method;

/**
 * <p> thinkpad工厂方法实现 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2022/4/23 15:20
 */
public class ThinkPadMethodFactoryImpl implements IMethodFactory {
    @Override
    public IKeyboard createKeyboard() {
        return new ThinkPadKeyboardImpl();
    }
}

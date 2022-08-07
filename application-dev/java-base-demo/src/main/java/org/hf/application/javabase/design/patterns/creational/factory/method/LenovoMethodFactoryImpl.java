package org.hf.application.javabase.design.patterns.creational.factory.method;

/**
 * <p> 联想工厂方法实现 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2022/4/23 15:15
 */
public class LenovoMethodFactoryImpl implements IMethodFactory {
    @Override
    public IKeyboard createKeyboard() {
        return new LenovoKeyboardImpl();
    }
}

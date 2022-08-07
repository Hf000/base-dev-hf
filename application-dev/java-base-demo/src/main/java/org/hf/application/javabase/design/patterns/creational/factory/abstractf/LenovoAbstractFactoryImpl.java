package org.hf.application.javabase.design.patterns.creational.factory.abstractf;

/**
 * <p>  </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2022/4/23 15:57
 */
public class LenovoAbstractFactoryImpl implements IAbstractFactory {
    @Override
    public IKeyboard createKeyboard() {
        return new LenovoKeyboardImpl();
    }

    @Override
    public IMonitor createMonitor() {
        return new LenovoMonitorImpl();
    }
}

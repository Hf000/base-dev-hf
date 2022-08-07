package org.hf.application.javabase.design.patterns.creational.factory.abstractf;

/**
 * <p>  </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2022/4/23 15:57
 */
public class ThinkPadAbstractFactoryImpl implements IAbstractFactory {
    @Override
    public IKeyboard createKeyboard() {
        return new ThinkPadKeyboardImpl();
    }

    @Override
    public IMonitor createMonitor() {
        return new ThinkPadMonitorImpl();
    }
}

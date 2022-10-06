package org.hf.application.javabase.design.patterns.creational.factory.abstractf;

/**
 * <p> 抽象工厂接口 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2022/4/23 15:53
 */
public interface IAbstractFactory {

    /**
     * 接口方法
     * @return IKeyboard
     */
    IKeyboard createKeyboard();

    /**
     * 接口方法
     * @return IMonitor
     */
    IMonitor createMonitor();

}

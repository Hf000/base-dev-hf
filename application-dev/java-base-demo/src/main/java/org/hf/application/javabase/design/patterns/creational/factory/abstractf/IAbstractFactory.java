package org.hf.application.javabase.design.patterns.creational.factory.abstractf;

/**
 * <p> 抽象工厂接口 </p>
 *
 * 将同一种品类的多个产品实现统一创建管理， 例如联想为一个品牌， 其下有联想鼠标、显示器、键盘等
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

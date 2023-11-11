package org.hf.application.javabase.design.patterns.creational.factory.method;

/**
 * <p> 工厂接口 </p>
 *
 * 将具体的一个产品实现统一创建管理， 例如键盘
 * @author hufei
 * @version 1.0.0
 * @date 2022/4/23 15:13
 */
public interface IMethodFactory {

    /**
     * 接口方法
     * @return IKeyboard
     */
    IKeyboard createKeyboard();
}

package org.hf.application.javabase.design.patterns.creational.factory.method;

/**
 * <p> 工厂接口 </p>
 *
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

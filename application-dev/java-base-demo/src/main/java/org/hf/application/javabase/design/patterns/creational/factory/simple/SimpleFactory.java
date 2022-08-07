package org.hf.application.javabase.design.patterns.creational.factory.simple;

import org.apache.commons.lang3.StringUtils;

/**
 * <p> 工厂模式-建单工厂 </p>
 * 不利于扩展
 * @author hufei
 * @version 1.0.0
 * @date 2022/4/23 14:57
 */
public class SimpleFactory {

    public static IKeyboard createInstance(String param) {
        IKeyboard keyboard = null;
        if (StringUtils.equalsIgnoreCase("ThinkPad", param)){
            keyboard = new ThinkPadKeyboardImpl();
        } else if (StringUtils.equalsIgnoreCase("Lenovo", param)) {
            keyboard = new LenovoKeyboardImpl();
        }
        return keyboard;
    }

}

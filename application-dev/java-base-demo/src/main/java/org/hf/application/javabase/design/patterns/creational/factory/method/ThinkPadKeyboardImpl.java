package org.hf.application.javabase.design.patterns.creational.factory.method;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>  </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2022/4/23 15:07
 */
@Slf4j
public class ThinkPadKeyboardImpl implements IKeyboard {
    @Override
    public void input() {
        log.info("ThinkPad工厂方法键盘");
    }
}

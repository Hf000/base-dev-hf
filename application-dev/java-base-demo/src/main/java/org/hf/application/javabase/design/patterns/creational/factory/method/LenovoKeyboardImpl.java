package org.hf.application.javabase.design.patterns.creational.factory.method;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>  </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2022/4/23 15:08
 */
@Slf4j
public class LenovoKeyboardImpl implements IKeyboard {
    @Override
    public void input() {
        log.info("Lenovo工厂方法键盘");
    }
}

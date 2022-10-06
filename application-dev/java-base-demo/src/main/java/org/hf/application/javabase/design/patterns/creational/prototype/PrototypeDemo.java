package org.hf.application.javabase.design.patterns.creational.prototype;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * <p> 原型模式demo </p>
 * 原型模式: 通过复制现有的实例来创建新的实例
 *
 * @author hufei
 * @date 2022/10/6 12:19
*/
@Slf4j
public class PrototypeDemo {

    public static void main(String[] args) {
        // 原型模式
        Prototype prototype = new Prototype();
        prototype.getPrototype();
    }
}
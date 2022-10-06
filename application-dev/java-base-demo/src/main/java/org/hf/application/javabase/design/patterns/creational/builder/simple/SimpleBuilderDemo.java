package org.hf.application.javabase.design.patterns.creational.builder.simple;

import lombok.extern.slf4j.Slf4j;
import org.hf.application.javabase.design.patterns.creational.builder.classic.ClassicBuilder;

/**
 * <p> 简单建造者demo </p>
 * 建造者模式: 封装一个复杂对象的构建过程, 并可以按步骤构造
 *
 * @author hufei
 * @version 1.0.0
 * @date 2022/4/23 16:46
 */
@Slf4j
public class SimpleBuilderDemo {

    public static void main(String[] args) {
        // 简单建造者
        SimpleBuilder.createInstance();
    }
}

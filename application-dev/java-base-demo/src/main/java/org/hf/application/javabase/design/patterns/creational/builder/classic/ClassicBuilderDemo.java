package org.hf.application.javabase.design.patterns.creational.builder.classic;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;

/**
 * <p> 经典建造者demo </p>
 * 建造者模式: 封装一个复杂对象的构建过程, 并可以按步骤构造
 *
 * @author hufei
 * @version 1.0.0
 * @date 2022/4/23 16:46
 */
@Slf4j
public class ClassicBuilderDemo {

    public static void main(String[] args) {
        // 经典建造者
        ClassicBuilder.createInstance();
    }
}

package org.hf.application.javabase.design.patterns.creational.builder.simple;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;

/**
 * <p> 设计模式-简单建造者 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2022/4/23 16:32
 */
@Slf4j
public class SimpleBuilder {

    /**
     * 根据需求创建对象
     */
    public static void createInstance() {
        Computer computer = new ComputerSimpleBuilder("因特尔","三星").setDisplay("三星24寸").setKeyboard("罗技").setUsbCount(2).build();
        log.info("简单建造者对象:{}", JSONObject.toJSONString(computer));
    }

}

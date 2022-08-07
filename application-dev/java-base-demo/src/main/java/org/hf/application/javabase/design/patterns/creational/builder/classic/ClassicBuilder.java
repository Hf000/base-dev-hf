package org.hf.application.javabase.design.patterns.creational.builder.classic;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;

/**
 * <p> 设计模式-经典建造者 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2022/4/23 16:46
 */
@Slf4j
public class ClassicBuilder {

    public static void createInstance() {
        // 1. 创建指导类对象
        ComputerDirector director = new ComputerDirector();
        // 2. 创建对应的建造者实现类
        ComputerBuilder lenovoBuilder = new LenovoComputerBuilder("I7处理器","海力士222");
        // 3. 安照指导类的方式创建需要的对象
        director.makeComputer(lenovoBuilder);
        // 4. 获得对象
        Computer lenovoComputer = lenovoBuilder.getComputer();
        log.info("经典建造者: {}", JSONObject.toJSONString(lenovoComputer));
    }

}

package org.hf.application.javabase.design.patterns.creational.prototype;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 设计模式-原型模式
 * @author HF
 */
@Slf4j
public class Prototype {

    public void getPrototype(){
        //创建原型
        PrototypeImpl prototypeImpl = new PrototypeImpl();
        //耗费资源的操作
        prototypeImpl.loadData();
        //使用原型对象构建新的对象
        PrototypeImpl copy = (PrototypeImpl) prototypeImpl.copy();
        List<String> content = copy.getContents();
        content.add(0,"《江城子·密州出猎》");
        content.add(1,"----------------------------------------------------------");
        for (String s : content) {
            log.info("{}",s);
        }
    }
}
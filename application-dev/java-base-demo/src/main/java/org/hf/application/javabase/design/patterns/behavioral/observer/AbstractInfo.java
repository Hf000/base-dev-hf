package org.hf.application.javabase.design.patterns.behavioral.observer;

/**
* <p> 抽象类 </p>
* @author hufei
* @date 2022/7/13 21:24
*/
public abstract class AbstractInfo {

    /**
     * 被监听的对象
     */
    private Clock clock;

    /**
     * 抽象方法
     */
    abstract  void message();
}

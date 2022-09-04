package org.hf.application.javabase.design.patterns.behavioral.observer;

/**
* <p>  </p>
* @author hufei
* @date 2022/7/13 21:24
*/
public abstract class AbstrackInfo {

    /**
     * 被监听的对象
     */
    private Clock clock;

    abstract  void message();
}

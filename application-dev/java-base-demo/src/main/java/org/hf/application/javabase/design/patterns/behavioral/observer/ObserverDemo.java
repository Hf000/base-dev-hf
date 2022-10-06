package org.hf.application.javabase.design.patterns.behavioral.observer;

/**
 * <p> 观察者模式demo </p>
 * 观察者模式: 对象间的一对多的依赖关系
 *
 * @author hufei
 * @version 1.0.0
 * @date 2022/9/4 20:42
 */
public class ObserverDemo {

    public static void main(String[] args) {
        Clock clock = new Clock();
        AbstractInfo eat = new EatInfo();
        AbstractInfo sp = new SleepInfo();
        clock.addInfo(eat);
        clock.addInfo(sp);
        clock.say();
    }

}

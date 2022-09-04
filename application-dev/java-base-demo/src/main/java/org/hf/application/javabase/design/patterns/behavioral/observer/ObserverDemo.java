package org.hf.application.javabase.design.patterns.behavioral.observer;

/**
 * <p> 观察者模式demo </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2022/9/4 20:42
 */
public class ObserverDemo {

    public static void main(String[] args) {
        Clock clock = new Clock();
        AbstrackInfo eat = new EatInfo();
        AbstrackInfo sp = new SleepInfo();
        clock.addInfo(eat);
        clock.addInfo(sp);
        clock.say();
    }

}

package org.hf.application.javabase.design.patterns.behavioral.observer;

/**
 * <p> 抽象实现 </p>
 * @author hufei
 * @date 2022/10/6 10:40
*/
public class SleepInfo extends AbstractInfo {

    @Override
    public void message(){
        System.out.println("大家午睡了！");
    }
}

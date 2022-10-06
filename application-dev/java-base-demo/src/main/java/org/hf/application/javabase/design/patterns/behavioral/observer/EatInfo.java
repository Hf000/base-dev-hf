package org.hf.application.javabase.design.patterns.behavioral.observer;

/**
 * <p> 抽象实现 </p>
 * @author hufei
 * @date 2022/10/6 10:39
*/
public class EatInfo extends AbstractInfo {

    @Override
    public void message(){
        System.out.println("大家吃饭了！");
    }
}

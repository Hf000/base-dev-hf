package org.hf.application.javabase.design.patterns.behavioral.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> 观察者模式demo </p>
 * @author hufei
 * @date 2022/9/4 20:40
*/
public class Clock {

    /**
     * 一对多，需要接到通知的对象
     */
    private List<AbstractInfo> INFO_LIST = new ArrayList<AbstractInfo>();

    public void say(){
        System.out.println("上班了！");
        //通知
        update();
    }

    /**
     * 添加要得到通知的对象
     * @param info 入参
     */
    public void addInfo(AbstractInfo info){
        INFO_LIST.add(info);
    }

    /**
     * 通知
     */
    public void update(){
        for (AbstractInfo info : INFO_LIST) {
            info.message();
        }
    }


}

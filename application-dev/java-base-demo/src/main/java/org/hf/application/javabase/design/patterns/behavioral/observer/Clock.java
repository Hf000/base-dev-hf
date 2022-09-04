package org.hf.application.javabase.design.patterns.behavioral.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> 观察者模式demo </p>
 * @author hufei
 * @date 2022/9/4 20:40
*/
public class Clock {

    //一对多，需要接到通知的对象
    private List<AbstrackInfo> infos = new ArrayList<AbstrackInfo>();

    public void say(){
        System.out.println("上班了！");
        //通知
        update();
    }

    //添加要得到通知的对象
    public void addInfo(AbstrackInfo info){
        infos.add(info);
    }

    //通知
    public void update(){
        for (AbstrackInfo info : infos) {
            info.message();
        }
    }


}

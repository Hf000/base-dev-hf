package org.hf.application.javabase.design.patterns.behavioral.state;

/**
* <p> 状态模式 </p>
* @author hufei
* @date 2022/7/13 21:29
*/
public class StateDemo {

    public static void main(String[] args) {
        // 创建一个对象
        Context context = new Context();
        // 创建一个状态对象
        StartState startState = new StartState();
        // 改变对象的状态
        startState.doAction(context);
        System.out.println(context.getState().toString());
        // 创建一个状态对象
        StopState stopState = new StopState();
        // 改变对象的状态
        stopState.doAction(context);
        System.out.println(context.getState().toString());
    }
}

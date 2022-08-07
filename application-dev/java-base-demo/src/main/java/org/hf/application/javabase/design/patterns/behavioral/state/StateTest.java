package org.hf.application.javabase.design.patterns.behavioral.state;

/**
* <p> 状态模式 </p>
* @author hufei
* @date 2022/7/13 21:29
*/
public class StateTest {

    public static void main(String[] args) {
        Context context = new Context();

        StartState startState = new StartState();
        startState.doAction(context);
        System.out.println(context.getState().toString());

        StopState stopState = new StopState();
        stopState.doAction(context);
        System.out.println(context.getState().toString());
    }
}

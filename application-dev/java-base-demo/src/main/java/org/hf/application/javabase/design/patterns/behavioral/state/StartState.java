package org.hf.application.javabase.design.patterns.behavioral.state;

/**
 * <p> 状态对象 </p>
 * @author hufei
 * @date 2022/9/4 20:37
*/
public class StartState implements State {

    @Override
    public void doAction(Context context) {
        System.out.println("Player is in start state");
        context.setState(this);
    }

    @Override
    public String toString() {
        return "Start State";
    }
}

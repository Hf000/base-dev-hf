package org.hf.application.javabase.design.patterns.behavioral.state;

/**
 * <p> 状态对象 </p>
 *
 * @author hufei
 * @date 2022/9/4 20:38
 */
public class StopState implements State {

    @Override
    public void doAction(Context context) {
        System.out.println("Player is in stop state");
        context.setState(this);
    }

    @Override
    public String toString() {
        return "Stop State";
    }
}

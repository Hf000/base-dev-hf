package org.hf.application.javabase.design.patterns.behavioral.state;

/**
 * <p> 设置对象 </p>
 *
 * @author hufei
 * @date 2022/10/6 12:08
 */
public class Context {

    /**
     * 状态变量
     */
    private State state;

    public Context() {
        this.state = null;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}

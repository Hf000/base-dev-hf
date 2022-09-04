package org.hf.application.javabase.design.patterns.behavioral.state;


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

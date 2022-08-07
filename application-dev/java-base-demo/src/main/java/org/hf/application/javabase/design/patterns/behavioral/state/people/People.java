package org.hf.application.javabase.design.patterns.behavioral.state.people;


public class People {

    //人拥有指定状态
    private State1 state1;

    public People() {
        this.state1 = null;
    }

    public State1 getState1() {
        return state1;
    }

    public void setState1(State1 state1) {
        this.state1 = state1;
    }
}

package org.hf.application.javabase.design.patterns.behavioral.state.people;


public class Night implements State1 {

    //改变状态
    @Override
    public void change(People people, State1 state1) {
        people.setState1(state1);
    }

    @Override
    public void execute() {
        System.out.println("晚上要睡觉！");
    }
}

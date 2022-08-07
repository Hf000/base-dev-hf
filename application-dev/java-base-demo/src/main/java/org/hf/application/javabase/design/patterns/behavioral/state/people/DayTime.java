package org.hf.application.javabase.design.patterns.behavioral.state.people;


public class DayTime implements State1 {

    /***
     * 改变状态-白天
     * @param people：要改变状态的对象
     * @param state1：变更成指定的状态
     */
    @Override
    public void change(People people, State1 state1) {
        people.setState1(state1);
        //立刻调用
        //execute();
    }

    @Override
    public void execute(){
        System.out.println("白天要吃饭！");
    }
}

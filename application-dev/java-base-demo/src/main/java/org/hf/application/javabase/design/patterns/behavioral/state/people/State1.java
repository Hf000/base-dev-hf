package org.hf.application.javabase.design.patterns.behavioral.state.people;


public interface State1 {

    /***
     * 改变状态
     * @param people：要改变状态的对象
     * @param state：变更成指定的状态
     */
    void change(People people, State1 state);

    //执行的行为
    void execute();
}

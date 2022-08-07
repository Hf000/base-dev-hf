package org.hf.application.javabase.design.patterns.behavioral.state.people;


public class StateTest1 {

    public static void main(String[] args) {
        //创建一个对象
        People people = new People();

        //创建一个状态
        State1 dayTime = new DayTime();
        //改变对象的状态
        dayTime.change(people,dayTime);
        //执行相关行为
        people.getState1().execute();

        //Night
        State1 night = new Night();
        night.change(people,night);

        //执行相关行为
        people.getState1().execute();
    }
}

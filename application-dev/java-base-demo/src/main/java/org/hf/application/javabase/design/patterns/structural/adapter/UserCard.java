package org.hf.application.javabase.design.patterns.structural.adapter;


public class UserCard implements UserCardService {

    @Override
    public void card(String name){
        System.out.println(name+"打卡成功！");
    }
}

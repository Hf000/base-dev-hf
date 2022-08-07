package org.hf.application.javabase.design.patterns.structural.proxy.jdk;


public class Landlord implements LandlordService {

    //收房租
    @Override
    public void pay(String name){
        System.out.println(name);
    }
}

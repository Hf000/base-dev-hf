package org.hf.application.javabase.apply.jdk8.defaultAndStatic;

public class MySon extends MyParent implements MyInterface{


    public static void main(String[] args) {
        MySon mySon=new MySon();
        mySon.method();
    }
}

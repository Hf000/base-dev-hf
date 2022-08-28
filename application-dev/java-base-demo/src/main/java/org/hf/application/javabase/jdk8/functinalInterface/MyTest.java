package org.hf.application.javabase.jdk8.functinalInterface;

import org.junit.Test;

public class MyTest {

    public void demo(MyFunctionalInterface functionalInterface){

        functionalInterface.exec();
    }

    @Test
    public void exec(){

        //jdk8之前
        demo(new MyFunctionalInterface(){

            @Override
            public void exec() {
                System.out.println("jdk8 before");
            }
        });

        //jdk8 lambda
        demo(()-> System.out.println("jdk8 later"));

    }
}

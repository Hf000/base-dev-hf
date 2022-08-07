package org.hf.application.javabase.apply.jdk8.defaultAndStatic;

public interface MyInterface {

    default void method(){
        System.out.println("myInterface default method");
    }
}

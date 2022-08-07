package org.hf.application.javabase.apply.jdk8.method;

public interface DemoService {

    //抽象方法
    String abstractMethod();

    //默认方法，扩展方法
    default String defaultMethod(){
        return "exec defaultMethod";
    }

    //静态方法，类方法
    static String staticMethod(){
        return "exec staticMethod";
    }
}

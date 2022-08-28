package org.hf.application.javabase.jdk8.lambda;

import java.util.Arrays;
import java.util.List;

public class SourceDemo {

    public static void demo(){

        String[] language = {"c", "c++",
                "c#",
                "java","python",
                "go","hive",
                "php"};

        List<String> list = Arrays.asList(language);

        System.setProperty("jdk.internal.lambda.dumpProxyClasses", "D://");

        list.forEach(s-> System.out.println(s));
    }

    public static void main(String[] args) {

        SourceDemo.demo();

    }
}

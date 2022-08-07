package org.hf.application.javabase.apply.jdk8.functinalInterface;

import java.util.function.Function;

public class MyFunctionDemo {

    public static Integer convert(String value, Function<String,Integer> function){

        return function.apply(value);
    }

    public static void main(String[] args) {

        String value ="666";

        Integer result = convert(value, (s) -> Integer.parseInt(value) + 222);

        System.out.println(result);
    }
}

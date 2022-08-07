package org.hf.application.javabase.apply.jdk8.functinalInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MyConsumerDemo {

    public static void foreach(List<String> list, Consumer<String> consumer){

        list.forEach(v->consumer.accept(v));
    }

    public static void main(String[] args) {

        List<String> arrays = new ArrayList<>();
        arrays.add("java");
        arrays.add("python");
        arrays.add("go");
        arrays.add("hive");

        foreach(arrays,(s)-> System.out.println(s+","));
    }
}

package org.hf.application.javabase.jdk8.stream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SkipDemo {

    public static void main(String[] args) {

        List<Integer> numberList = Arrays.asList(1, 2, 3, 4, 1, 2, 3, 4);

        List<Integer> result = numberList.stream().skip(2).limit(5).collect(Collectors.toList());

        System.out.println(result);
    }
}

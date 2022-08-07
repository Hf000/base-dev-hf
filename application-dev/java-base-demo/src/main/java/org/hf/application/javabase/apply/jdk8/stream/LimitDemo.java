package org.hf.application.javabase.apply.jdk8.stream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LimitDemo {

    public static void main(String[] args) {

        List<Integer> numberList = Arrays.asList(1, 2, 3, 4, 1, 2, 3, 4);

        List<Integer> result = numberList.stream().limit(5).collect(Collectors.toList());

        System.out.println(result);
    }
}

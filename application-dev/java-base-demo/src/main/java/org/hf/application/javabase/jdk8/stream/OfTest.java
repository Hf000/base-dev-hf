package org.hf.application.javabase.jdk8.stream;

import java.util.Arrays;
import java.util.stream.Stream;

public class OfTest {

    public static void main(String[] args) {

        Stream<String> stringStream = Stream.of("1", "2", "3");
        stringStream.forEach(v-> System.out.println(v));

        Stream<Object> objectStream = Stream.of("1", 2, true, new St());

        Integer[] numbers = new Integer[]{1,2,3,4,5,6};
        Stream<Integer> stream = Arrays.stream(numbers);

    }
}

class St{}

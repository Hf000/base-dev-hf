package org.hf.application.javabase.apply.jdk8.stream;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReduceTest {

    public static void main(String[] args) {

        List<Integer> numbers = new ArrayList<>();
        numbers.add(1);
        numbers.add(2);
        numbers.add(3);
        numbers.add(4);
        numbers.add(5);
        numbers.add(6);
        numbers.add(7);

        //Integer reduce = numbers.stream().reduce(0, (a, b) -> a + b);

        //Integer reduce = numbers.stream().reduce(0, Integer::sum);
        //System.out.println(reduce);

        /*Optional<Integer> optional = numbers.stream().reduce(Integer::sum);
        if (optional.isPresent()){
            Integer integer = optional.get();
            System.out.println(integer);
        }*/

        //获取最大值
        //Optional<Integer> maxOptional = numbers.stream().reduce(Integer::max);

       /* Optional<Integer> maxOptional = numbers.stream().max(Integer::compareTo);
        if (maxOptional.isPresent()){
            System.out.println(maxOptional.get());
        }*/

        //获取最小值
        Optional<Integer> minOptional = numbers.stream().min(Integer::compareTo);
        System.out.println(minOptional.get());

    }
}

package org.hf.application.javabase.jdk8.stream;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <p> stream流归约操作:reduce()方法进行累计求和,取最大值,取最小值 </p>
 *
 * @author hufei
 * @date 2022/9/3 18:03
 */
public class ReduceDemo {

    public static void main(String[] args) {
        List<Integer> numbers = new ArrayList<>();
        numbers.add(1);
        numbers.add(2);
        numbers.add(3);
        numbers.add(4);
        numbers.add(5);
        numbers.add(6);
        numbers.add(7);
        // 求和
        //Integer reduce = numbers.stream().reduce(0, (a, b) -> a + b);
        //Integer reduce = numbers.stream().reduce(0, Integer::sum);
        //System.out.println(reduce);
        Optional<Integer> optional = numbers.stream().reduce(Integer::sum);
        if (optional.isPresent()) {
            System.out.println("求和结果:" + optional.get());
        }
        //获取最大值
        Optional<Integer> maxOptional = numbers.stream().reduce(Integer::max);
//        Optional<Integer> maxOptional = numbers.stream().max(Integer::compareTo);
        if (maxOptional.isPresent()) {
            System.out.println("求最大值结果" + maxOptional.get());
        }
        //获取最小值
//        Optional<Integer> minOptional = numbers.stream().min(Integer::compareTo);
        Optional<Integer> minOptional = numbers.stream().reduce(Integer::min);
        System.out.println("求最小值结果" + minOptional.get());

    }
}

package org.hf.application.javabase.jdk8.stream;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * <p> stream基于值创建流 </p>
 *
 * @author hufei
 * @date 2022/9/3 17:34
 */
public class OfDemo {

    public static void main(String[] args) {
        // 构建字符串流
        Stream<String> stringStream = Stream.of("1", "2", "3");
        stringStream.forEach(System.out::println);
        // 构建Object对象流
        Stream<Object> objectStream = Stream.of("1", 2, true, new St());
        // 基于数组构建流
        Integer[] numbers = new Integer[]{1, 2, 3, 4, 5, 6};
        Stream<Integer> stream = Arrays.stream(numbers);
    }
}

class St {
}

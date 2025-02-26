package org.hf.application.javabase.jdk8.functinalInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * <p> 消费型函数式接口:Consumer函数接口的accept()用于进行自定义逻辑处理,无返回值 </p >
 * jdk应用: Iterable中的forEach方法接收的就是一个Consumer<T>函数, ArrayList就是重写了此方法
 * @author hufei
 * @date 2022/9/3 17:15
*/
public class MyConsumerDemo {

    /**
     * 循环
     * @param list 集合
     * @param consumer Consumer接口函数的具体实现
     */
    public static void foreach(List<String> list, Consumer<String> consumer){
        // 实际调用的是consumer::accept
        list.forEach(consumer);
    }

    public static void foreachConsumer(List<String> list, Consumer<List<String>> consumer){
        consumer.accept(list);
    }

    public static void main(String[] args) {
        List<String> arrays = new ArrayList<>();
        arrays.add("java");
        arrays.add("python");
        arrays.add("go");
        arrays.add("hive");
        // 循环获取集合的值
        foreach(arrays, (s)-> System.out.println(s+","));
        System.out.println("-------------------------------------------------------");
        foreachConsumer(arrays, v -> v.forEach(System.out::println));
    }
}

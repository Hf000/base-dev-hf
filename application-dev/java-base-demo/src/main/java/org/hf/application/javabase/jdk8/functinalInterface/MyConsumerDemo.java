package org.hf.application.javabase.jdk8.functinalInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * <p> 函数式接口:Consumer函数接口的accept()用于进行获取数据操作,无返回值 </p>
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

    public static void main(String[] args) {
        List<String> arrays = new ArrayList<>();
        arrays.add("java");
        arrays.add("python");
        arrays.add("go");
        arrays.add("hive");
        // 循环获取集合的值
        foreach(arrays,(s)-> System.out.println(s+","));
    }
}

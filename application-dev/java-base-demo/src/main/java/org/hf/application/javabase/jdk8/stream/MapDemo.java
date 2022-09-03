package org.hf.application.javabase.jdk8.stream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p> stream流映射操作:map()方法获取对象中指定属性的值,返回值Stream </p>
 * map()方法接收Function函数接口对象实例进行类型转换
 *
 * @author hufei
 * @date 2022/9/3 17:02
 */
public class MapDemo {

    public static void main(String[] args) {
        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student(1, "张三", "M", 19, true));
        studentList.add(new Student(2, "李四", "M", 18, false));
        studentList.add(new Student(3, "王五", "F", 21, true));
        studentList.add(new Student(4, "赵六", "F", 20, false));
        //提出name信息，转换为一个新的集合
        List<String> result = studentList.stream().map(Student::getName).collect(Collectors.toList());
        System.out.println(result);
        List<String> numberList = Arrays.asList("1", "2", "3", "4");
        int sum = numberList.stream().mapToInt(s -> Integer.parseInt(s)).sum();
        System.out.println(sum);
    }
}

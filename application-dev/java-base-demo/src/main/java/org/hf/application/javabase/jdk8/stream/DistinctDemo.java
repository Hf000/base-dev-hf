package org.hf.application.javabase.jdk8.stream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p> stream流筛选操作:distinct()方法数据去重,返回值Stream </p>
 * distinct()方法内部是基于LinkedHashSet对流中数据进行去重,并最终返回一个新的流
 *
 * @author hufei
 * @date 2022/9/3 16:36
 */
public class DistinctDemo {

    public static void main(String[] args) {

        //对数据模2后结果去重
        List<Integer> numberList = Arrays.asList(1, 2, 3, 4, 1, 2, 3, 4);

       /* List<Integer> integers = demo1(numberList);
        System.out.println(integers);

        List<Integer> result = demo2(numberList);
        System.out.println(result);*/

        List<Integer> result = demo3(numberList);
        System.out.println(result);

        // 对对象进行去重,需要重写对象的equals和hashCode方法
        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student(1, "张三", "M", 19, true));
        studentList.add(new Student(2, "李四", "M", 18, false));
        studentList.add(new Student(3, "王五", "F", 21, true));
        studentList.add(new Student(4, "赵六", "F", 20, false));
        studentList.add(new Student(1, "张三", "M", 19, true));
        studentList.add(new Student(2, "李四", "M", 18, false));
        studentList.add(new Student(3, "王五", "F", 21, true));
        studentList.add(new Student(4, "赵六", "F", 20, false));
        // 对集合对象去重
        List<Student> resultTwo = studentList.stream().distinct().collect(Collectors.toList());
        System.out.println(resultTwo);

    }

    /**
     * java7 将一个集合的值赋给另一个集合
     *
     * @param numberList 入参
     * @return List<Integer>
     */
    public static List<Integer> demo1(List<Integer> numberList) {
        List<Integer> resultList = new ArrayList<>();
        for (Integer number : numberList) {
            if (number % 2 == 0) {
                resultList.add(number);
            }
        }
        List<Integer> newList = new ArrayList<>();
        for (Integer number : numberList) {
            if (!newList.contains(number)) {
                newList.add(number);
            }
        }
        return newList;
    }

    /**
     * java7 利用set去重
     *
     * @param numberList 入参
     * @return List<Integer>
     */
    public static List<Integer> demo2(List<Integer> numberList) {
        List<Integer> newList = new ArrayList();
        Set set = new HashSet();
        for (Integer number : numberList) {
            if (number % 2 == 0) {
                if (set.add(number)) {
                    newList.add(number);
                }
            }
        }
        return newList;
    }

    /**
     * 通过stream中的distinct完成数据去重
     *
     * @param numberList 入参
     * @return List<Integer>
     */
    public static List<Integer> demo3(List<Integer> numberList) {
        List<Integer> result = numberList.stream()
                .filter(n -> n % 2 == 0)
                .distinct()
                .collect(Collectors.toList());
        return result;
    }
}

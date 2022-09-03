package org.hf.application.javabase.jdk8.stream;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p> stream流筛选操作:filter()方法过滤出符合条件的数据,返回值Stream </p>
 * filter()方法接收Predicate函数接口对象进行数据判断
 *
 * @author hufei
 * @date 2022/9/3 16:42
 */
public class FilterDemo {

    public static void main(String[] args) {
        //获取所有年龄20岁以下的学生
        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student(1, "张三", "M", 19));
        studentList.add(new Student(1, "李四", "M", 18));
        studentList.add(new Student(1, "王五", "F", 21));
        studentList.add(new Student(1, "赵六", "F", 20));
        //java7
        List<Student> resultList = new ArrayList<>();
        for (Student student : studentList) {
            if (student.getAge() < 20) {
                resultList.add(student);
            }
        }
        //java8 Stream
        //中间操作
//        Stream<Student> studentStream = studentList.stream().filter(s -> s.getAge() < 20);
//        //终端操作
//        List<Student> list = studentStream.collect(Collectors.toList());
        List<Student> result = studentList.stream().filter(s -> s.getAge() < 20).collect(Collectors.toList());
        System.out.println(result);
        //获取所有及格学生的信息
        List<Student> studentListTwo = new ArrayList<>();
        studentListTwo.add(new Student(1, "张三", "M", 19, true));
        studentListTwo.add(new Student(1, "李四", "M", 18, false));
        studentListTwo.add(new Student(1, "王五", "F", 21, true));
        studentListTwo.add(new Student(1, "赵六", "F", 20, false));
        //java7写法
        List<Student> resultListTwo = new ArrayList<>();
        for (Student student : studentListTwo) {
            if (student.getIsPass()) {
                resultListTwo.add(student);
            }
        }
        //Stream并行流操作
        List<Student> resultTwo = studentListTwo.parallelStream().filter(Student::getIsPass).collect(Collectors.toList());
        System.out.println(resultTwo);
    }
}

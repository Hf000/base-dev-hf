package org.hf.application.javabase.jdk8.stream;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p> stream流和非stream流操作对比测试 </p>
 * 不用stream流方式进行集合操作,采用外部迭代方式,及早取值
 * 用stream流方式进行集合操作,采用内部迭代方式,惰性取值
 *
 * @author hufei
 * @version 1.0.0
 * @date 2022/9/3 17:38
 */
public class StreamContrastOperationTest {

    @Test
    public void operateTest() {
        //java7  查询年龄小于20岁的学生,并且根据年龄进行排序，得到学生姓名，生成新集合
        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student(1, "张三", "M", 19));
        studentList.add(new Student(1, "李四", "M", 18));
        studentList.add(new Student(1, "王五", "F", 21));
        studentList.add(new Student(1, "赵六", "F", 20));
        //条件筛选
        List<Student> result = new ArrayList<>();
        for (Student student : studentList) {
            if (student.getAge() < 20) {
                result.add(student);
            }
        }
        //排序
        Collections.sort(result, new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                return Integer.compare(o1.getAge(), o2.getAge());
            }
        });
        //获取学生姓名
        List<String> nameList = new ArrayList<>();
        for (Student student : result) {
            nameList.add(student.getName());
        }
        System.out.println(nameList.toString());
    }

    @Test
    public void streamOperateTest() {
        //java8  查询年龄小于20岁的学生,并且根据年龄进行排序，得到学生姓名，生成新集合
        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student(1, "张三", "M", 19));
        studentList.add(new Student(1, "李四", "M", 18));
        studentList.add(new Student(1, "王五", "F", 21));
        studentList.add(new Student(1, "赵六", "F", 20));
        List<String> result = studentList.stream()
                //过滤出年龄小于20岁的学生
                .filter(s -> s.getAge() < 20)
                // 根据年龄进行排序
                .sorted(Comparator.comparing(Student::getAge))
                //得到学生姓名
                .map(Student::getName)
                //生成新集合
                .collect(Collectors.toList());
        System.out.println(result);
    }

}

package org.hf.application.javabase.jdk8.stream;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> stream流匹配操作:allMatch()方法判断条件是否匹配所有元素,返回值boolean </p>
 *
 * @author hufei
 * @date 2022/9/3 16:13
 */
public class AllMatchDemo {

    public static void main(String[] args) {
        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student(1, "张三", "M", 19, true));
        studentList.add(new Student(2, "李四", "M", 18, true));
        studentList.add(new Student(3, "王五", "F", 21, true));
        studentList.add(new Student(4, "赵六", "F", 20, false));
        if (studentList.stream().allMatch(Student::getIsPass)) {
            System.out.println("所有的学生都及格");
        } else {
            System.out.println("至少有一个学生不及格");
        }
    }
}

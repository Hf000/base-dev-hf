package org.hf.application.javabase.jdk8.stream;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <p> stream流查找操作 </p>
 * 1.findAny()方法获取流中随机的某一个元素,返回值Optional,短路取值
 * 2.findFirst()方法获取流中的第一个元素,返回值Optional
 *
 * @author hufei
 * @date 2022/9/3 16:48
 */
public class FindDemo {

    public static void main(String[] args) {
        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student(1, "张三", "M", 18, true));
        studentList.add(new Student(2, "李四", "M", 19, true));
        studentList.add(new Student(3, "王五", "F", 21, true));
        studentList.add(new Student(4, "赵六1", "F", 15, false));
        studentList.add(new Student(5, "赵六2", "F", 16, false));
        studentList.add(new Student(6, "赵六3", "F", 17, false));
        studentList.add(new Student(7, "赵六4", "F", 18, false));
        studentList.add(new Student(8, "赵六5", "F", 19, false));
        // 随机获取一个元素
        Optional<Student> optionalAny = studentList.stream().filter(s -> s.getAge() < 20).findAny();
        optionalAny.ifPresent(student -> System.out.println("findAny:" + student));
        // 获取第一个元素
        Optional<Student> optionalFirst = studentList.stream().filter(s -> s.getAge() < 20).findFirst();
        optionalFirst.ifPresent(student -> System.out.println("findFirst:" + student));
        // 测试findAny在并行流中随机获取,在串行流中取第一个元素
        for (int i = 0; i < 1000; i++) {
            Optional<Student> optional = studentList.parallelStream().filter(s -> s.getAge() < 20).findAny();
            if (optional.isPresent()) {
                Student student = optional.get();
                System.out.println(student);
            }
        }
    }
}

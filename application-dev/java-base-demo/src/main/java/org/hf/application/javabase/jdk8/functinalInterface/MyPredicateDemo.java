package org.hf.application.javabase.jdk8.functinalInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * <p> 断言型函数式接口:Predicate函数接口的test()方法用于进行指定逻辑的条件判断操作,有返回值:boolean类型 </p >
 *  jdk应用: Stream中的filter方法
 * @author hufei
 * @date 2022/9/3 17:28
 */
public class MyPredicateDemo {

    /**
     * 过滤方法
     *
     * @param studentList 需要过滤的对象
     * @param predicate   Predicate函数接口的具体实现对象
     * @return 过滤后的对象
     */
    public static List<Student> filter(List<Student> studentList, Predicate<Student> predicate) {
        List<Student> list = new ArrayList<>();
        studentList.forEach(s -> {
            if (predicate.test(s)) {
                list.add(s);
            }
        });
        return list;
    }

    public static void main(String[] args) {
        List<Student> students = new ArrayList<>();
        students.add(new Student(1, "张三", "M"));
        students.add(new Student(2, "李四", "M"));
        students.add(new Student(3, "王五", "F"));
        // 过滤出集合中符合条件的对象
        List<Student> result = filter(students, (s) -> "F".equals(s.getSex()));
        System.out.println(result.toString());
    }
}

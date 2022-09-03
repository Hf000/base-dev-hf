package org.hf.application.javabase.jdk8.stream;

import org.hf.application.javabase.jdk8.lambda.Student;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * <p> 自定义stream收集器 </p>
 *
 * @author hufei
 * @date 2022/9/3 17:14
 */
public class MyCollectorDemo implements Collector<Student, List<Student>, List<Student>> {

    @Override
    public Supplier<List<Student>> supplier() {
        return ArrayList::new;
    }

    @Override
    public BiConsumer<List<Student>, Student> accumulator() {
        return ((studentList, student) -> {
            if (student.getIsPass()) {
                studentList.add(student);
            }
        });
    }

    @Override
    public BinaryOperator<List<Student>> combiner() {
        return null;
    }

    @Override
    public Function<List<Student>, List<Student>> finisher() {
        return Function.identity();
    }

    @Override
    public Set<Characteristics> characteristics() {
        return EnumSet.of(Characteristics.IDENTITY_FINISH, Characteristics.UNORDERED);
    }

    public static void main(String[] args) {
        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student(1, "张三", "M", 19, true));
        studentList.add(new Student(2, "李四", "M", 18, true));
        studentList.add(new Student(3, "王五", "F", 21, true));
        studentList.add(new Student(4, "赵六", "F", 20, false));
        // 自定义收集器, 返回所有合格的学员
        List<Student> list = studentList.stream().collect(new MyCollectorDemo());
        System.out.println(list);
    }
}

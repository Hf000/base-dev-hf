package org.hf.application.javabase.apply.jdk8.stream;

import org.hf.application.javabase.apply.jdk8.lambda.Student;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class MyCollector implements Collector<Student, List<Student>,List<Student>> {

    @Override
    public Supplier<List<Student>> supplier() {
        return ArrayList::new;
    }

    @Override
    public BiConsumer<List<Student>, Student> accumulator() {
        return ((studentList, student) -> {
            if (student.getIsPass()){
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
        return EnumSet.of(Characteristics.IDENTITY_FINISH,Characteristics.UNORDERED);
    }
}

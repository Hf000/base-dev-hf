package org.hf.application.javabase.jdk8.lambda;

import java.util.Arrays;
import java.util.List;

/**
 * <p> 生成lambda表达式代码字节码文件 </p >
 * Jdk8中Lambda表达式在执行的时候，会调用LambdaMetafactory.metafactory(..)方法动态的生成内部类，
 * 在方法内调用一个静态方法(此方法就是在原有类编译的时候会生成的一个方法)，内部类里的调用方法块并不是动态生成的，
 * 只是在原class里已经编译生成了一个静态的方法，内部类只需要调用该静态方法。
 *
 * @author hufei
 * @date 2022/9/3 18:16
 */
public class SourceDemo {

    public static void demo() {
        String[] language = {"c", "c++",
                "c#",
                "java", "python",
                "go", "hive",
                "php"};
        List<String> list = Arrays.asList(language);
        // 生成代理类字节码文件,可以通过这个命令编译生成的class文件: javap -c -p class文件名称
        System.setProperty("jdk.internal.lambda.dumpProxyClasses", "D://");
        list.forEach(System.out::println);
    }

    public static void main(String[] args) {
        SourceDemo.demo();
    }
}

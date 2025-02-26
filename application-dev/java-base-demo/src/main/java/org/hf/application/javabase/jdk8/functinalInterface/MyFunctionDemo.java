package org.hf.application.javabase.jdk8.functinalInterface;

import java.util.function.Function;

/**
 * <p> 函数型函数式接口: Function函数接口的apply()用于进行指定逻辑处理,有返回值,返回处理后的接口 </p >
 * jdk应用: Stream中的map与flatMap方法
 * @author hufei
 * @date 2022/9/3 17:25
*/
public class MyFunctionDemo {

    /**
     * 进行对象转换
     * @param value 需要转换的对象
     * @param function Function函数接口的具体实现
     * @return 返回转换后的对象
     */
    public static Integer convert(String value, Function<String,Integer> function){
        return function.apply(value);
    }

    public static void main(String[] args) {
        String value ="666";
        // 进行对象转换
        Integer result = convert(value, (s) -> Integer.parseInt(value) + 222);
        System.out.println(result);
    }
}

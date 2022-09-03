package org.hf.application.javabase.jdk8.stream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p> stream流切片操作:limit()方法数据截取,返回值stream </p>
 * skip()和limit()方法实际底层调用是同一个方法,只是执行limit方法时skip传参是0,而执行skip方法时limit传参是-1
 *
 * @author hufei
 * @date 2022/9/3 16:59
 */
public class LimitDemo {

    public static void main(String[] args) {
        List<Integer> numberList = Arrays.asList(1, 2, 3, 4, 1, 2, 3, 4);
        List<Integer> result = numberList.stream().limit(5).collect(Collectors.toList());
        System.out.println(result);
    }
}

package org.hf.application.javabase.jdk8.functinalInterface;

import java.util.function.Supplier;

/**
 * <p> 供给型(只有输出(出参),没有输入(入参))函数式接口:Supplier函数接口的get()方法用于根据指定逻辑获取相应的结果,有返回值 </p >
 *  jdk应用: Optional类中的orElseGet方法
 * @author hufei
 * @date 2022/9/3 17:31
 */
public class MySupplierDemo {

    /**
     * 获取最小值
     *
     * @param supplier Supplier函数接口的具体实现
     * @return 获取到的结果
     */
    public static Integer getMin(Supplier<Integer> supplier) {
        return supplier.get();
    }

    public static void main(String[] args) {
        int[] arr = {100, 20, 50, 30, 99, 101, -50};
        // 获取集合中的最小值
        Integer result = getMin(() -> {
            int min = arr[0];
            for (int i : arr) {
                if (i < min) {
                    min = i;
                }
            }
            return min;
        });
        System.out.println(result);
    }
}

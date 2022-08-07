package org.hf.application.javabase.apply.jdk8.functinalInterface;

import java.util.function.Supplier;

public class MySupplierDemo {

    public static Integer getMin(Supplier<Integer> supplier){

        return supplier.get();
    }

    public static void main(String[] args) {

        int[] arr = {100,20,50,30,99,101,-50};

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

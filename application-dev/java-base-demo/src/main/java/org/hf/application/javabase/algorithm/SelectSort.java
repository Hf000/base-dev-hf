package org.hf.application.javabase.algorithm;

/**
 * <p> 选择排序 </p>
 * @author hufei
 * @version 1.0.0
 * @date 2022/4/23 16:46
 */
public class SelectSort {
    public static void main(String[] args) {
        int[] arr = {0, 12, 5, 8, 3, 2, 6};
        sort(arr);
        for (int i : arr) {
            System.out.println(i);
        }
    }

    private static int[] sort(int[] arr) {
        if (arr != null && arr.length > 0) {
            int idx = 0;
            for (int i = 0; i < arr.length; i++) {
                idx = i;
                for (int j = i + 1; j < arr.length; j++) {
                    if (arr[j] < arr[idx]) {
                        idx = j;
                    }
                }
                if (i != idx) {
//                    arr[idx] = arr[idx]^arr[i];
//                    arr[i] =arr[idx]^arr[i];
//                    arr[idx] = arr[idx]^arr[i];
                    int temp = arr[i];
                    arr[i] = arr[idx];
                    arr[idx] = temp;
                }
            }
        }
        return arr;
    }
}

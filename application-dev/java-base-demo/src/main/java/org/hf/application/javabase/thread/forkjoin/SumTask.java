package org.hf.application.javabase.thread.forkjoin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

/**
 * <p> 采用ForkJoin计算1-1000的和 </p >
 * @author HF
 * @date 2022-10-28
 **/
public class SumTask {

    public static void main(String[] args) {
        // 创建ForkJoin线程池
        ForkJoinPool pool = new ForkJoinPool();
        Future<Integer> taskFuture = pool.submit(new SubTask(1, 1000));
        try {
            Integer result = taskFuture.get();
            System.out.println("result = " + result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace(System.out);
        }
        // 释放资源
        pool.shutdown();
    }

    private static final Integer MAX = 100;

    static class SubTask extends RecursiveTask<Integer> {
        private static final long serialVersionUID = 7981012864387137318L;
        /**
         * 子任务开始计算的值
         */
        private final Integer start;
        /**
         * 子任务结束计算的值
         */
        private final Integer end;

        public SubTask(Integer start, Integer end) {
            this.start = start;
            this.end = end;
        }

        @Override
        protected Integer compute() {
            if (end - start < MAX) {
                // 小于边界，开始计算
                System.out.println("start = " + start + ";end = " + end);
                // 此区间的数字累加的和变量
                int totalValue = 0;
                // 计算此区间的数字累加之和,for循环从开始相加到结束的数字
                for (int index = this.start; index <= this.end; index++) {
                    totalValue += index;
                }
                return totalValue;
            } else {
                //否则，中间劈开继续拆分
                SubTask subTask1 = new SubTask(start, (start + end) / 2);
                subTask1.fork();
                SubTask subTask2 = new SubTask((start + end) / 2 + 1, end);
                subTask2.fork();
                // 合并结果
                return subTask1.join() + subTask2.join();
            }
        }
    }
}
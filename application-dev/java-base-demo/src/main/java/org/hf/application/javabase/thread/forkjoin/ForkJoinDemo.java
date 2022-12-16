package org.hf.application.javabase.thread.forkjoin;

import lombok.SneakyThrows;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

/**
 * <p> ForkJoin进行数据异步处理后合并, 继承RecursiveTask有返回值,继承RecursiveAction无返回值 </p >
 *
 * @author HF
 * @date 2022-10-28
 **/
@SuppressWarnings({"all"})
public class ForkJoinDemo {

    @SneakyThrows
    public static void main(String[] args) {
        // 公用线程池
//        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        // 创建ForkJoin线程池,指定线程数量为2
        ForkJoinPool pool = new ForkJoinPool(2);
        // 有返回值实现
//        recursiveTaskTest(pool);
        // 无返回值实现
        recursiveActionTest(pool);
        // 释放资源
        pool.shutdown();
    }

    /**
     * RecursiveAction实现任务拆分测试,无返回值
     * 需要进行所有任务完成的操作处理,否则任务会被提前结束,ForkJoinPool执行任务的线程是守护线程,如果程序中只有守护线程,jvm会退出
     * @param pool 入参
     */
    @SneakyThrows
    private static void recursiveActionTest(ForkJoinPool pool) {
        // 等待所有任务完方式一: 成注意这里需要调用线程池的awaitTermination()方法来等待线程执行完毕
        pool.submit(new TaskDemo(1, 1000));
        // 等待所有线程执行完毕
        boolean completed = pool.awaitTermination(10, TimeUnit.SECONDS);
        if (completed) {
            System.out.println("任务执行完成");
        }
        // 等待所有任务完方式二: 派生任务并等待结果, 此方式要求拆分任务调用fork()方法之后,调用join()方法等待任务完成, invoke是同步执行,主线程会阻塞
//        pool.invoke(new TaskDemo(1, 1000));
        // 等待所有任务完成方式三: 让主线程阻塞一下
//        TimeUnit.SECONDS.sleep(5);
    }

    /**
     * RecursiveTask实现任务拆分测试,有返回值
     * 这里不会提前结束任务,是因为需要等待获取返回值
     * @param pool 入参
     */
    @SneakyThrows
    private static void recursiveTaskTest(ForkJoinPool pool) {
        Future<Integer> future = pool.submit(new Task(1, 1000));
        System.out.println(future.get());
    }
}

/**
 * 有返回值, 适用于查询,拆分查询后结果合并
 */
class Task extends RecursiveTask<Integer> {

    private static final long serialVersionUID = -3039259967406354716L;
    /**
     * 是否拆分任务的标识, 这里标识为2,任务并不会拆分500次, 因为开始减去结束的结果可能为1或者2
     */
    final int segment = 2;
    /**
     * 开始
     */
    private final int start;
    /**
     * 结束
     */
    private final int end;

    public Task(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        if (end - start > segment) {
            // 获取开始和结束的中位数
            int middle = (start + end) / 2;
            // 以中位数为边界,再拆分任务
            Task task1 = new Task(start, middle);
            Task task2 = new Task(middle + 1, end);
            task1.fork();
            task2.fork();
            System.out.println("fork:" + start + "," + middle + "#" + (middle + 1) + "," + end);
            // 合并结果
            return task1.join() + task2.join();
        } else {
            int result = 0;
            for (int num = start; num <= end; num++) {
                result += num;
            }
            return result;
        }
    }
}

/**
 * 没有返回值, 适用于不返回结果的增删改操作,修改数据库,在拆分任务前设置手动开启事务,无报错则手动提交事务
 */
@SuppressWarnings({"all"})
class TaskDemo extends RecursiveAction {

    /**
     * 是否拆分任务的标识
     */
    final int segment = 2;
    /**
     * 开始
     */
    private final int start;
    /**
     * 结束
     */
    private final int end;

    public TaskDemo(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected void compute() {
        if (end - start > segment) {
            // 获取开始和结束的中位数
            int middle = (start + end) / 2;
            // 以中位数为边界,再拆分任务
            TaskDemo task1 = new TaskDemo(start, middle);
            TaskDemo task2 = new TaskDemo(middle + 1, end);
            task1.fork();
            task2.fork();
            System.out.println("fork:" + start + "," + middle + "#" + (middle + 1) + "," + end);
//            task1.join();
//            task2.join();
        } else {
            for (int num = start; num <= end; num++) {
                // 进行数据操作
//                System.out.println("fork: start = " + start + "," + " # end = " + end);
            }
        }
    }
}
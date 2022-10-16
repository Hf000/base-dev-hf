package org.hf.application.javabase.thread.forkjoin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

public class TaskMain {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ForkJoinPool pool = new ForkJoinPool(2);

        Future<Integer> future = pool.submit(new Task(1,1000));


        System.out.println(future.get());


    }
}

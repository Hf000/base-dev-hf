package org.hf.application.javabase.thread.forkjoin;

import java.util.concurrent.RecursiveTask;

public class Task extends RecursiveTask<Integer> {
    final int segment = 2;
    private int start,end;

    public Task(int start , int end){
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        if (end - start > segment){

            int middle = (start + end ) /2;

            Task task1 = new Task(start,middle);
            Task task2 = new Task(middle+1,end);

            task1.fork();
            task2.fork();
            System.out.println("fork:"+start+","+middle+"#"+(middle+1)+","+end);
            return task1.join() + task2.join();

        }else {

            int r = 0;

            for (int j = start;j<=end;j++){
                r += j;
            }

            return r;

        }




    }
}

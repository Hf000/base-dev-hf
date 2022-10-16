package org.hf.application.javabase.thread.demo;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

//自定义类，重写terminated方法
public class MyExecutorService extends ThreadPoolExecutor {

    public MyExecutorService(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    @Override
    protected void terminated() {
        super.terminated();
        System.out.println("treminated");
    }
    
    //调用 shutdownNow， ternimated方法被调用打印
    public static void main(String[] args) throws InterruptedException {
        MyExecutorService service = new MyExecutorService(1,2,10000,TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>(5));
        service.shutdownNow();
    }
}


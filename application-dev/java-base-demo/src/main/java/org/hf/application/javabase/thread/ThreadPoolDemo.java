package org.hf.application.javabase.thread;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import lombok.SneakyThrows;
import org.hf.application.javabase.utils.ThreadPoolUtil;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p> 线程池demo </p >
 * 线程池创建/关闭工具 {@link org.hf.application.javabase.utils.ThreadPoolUtil}
 * 线程池状态:
 *  RUNNING: 初始化状态, 线程池被一旦被创建，就处于RUNNING状态，并且线程池中的任务数为0。RUNNING状态下，能够接收新任务，以及对已添加的任务进行处理。
 *  SHUTDOWN: SHUTDOWN状态时，不接收新任务，但能处理已添加的任务。调用线程池的shutdown()接口时，线程池由RUNNING -> SHUTDOWN。
 *  STOP: 不接收新任务，不处理已添加的任务，并且会中断正在处理的任务。调用线程池的shutdownNow()接口时，线程池由(RUNNING 或 SHUTDOWN ) -> STOP
 *      注意:这里的运行中的任务还会打印,直到结束,因为调的是Thread.interrupt()只是标记了中断状态
 *  TIDYING: 所有的任务已终止，队列中的”任务数量”为0，线程池会变为TIDYING。线程池变为TIDYING状态时，会执行钩子函数terminated()，可以通过重载terminated()函数来实现自定义行为
 *  TERMINATED: 线程池处在TIDYING状态时，执行完terminated()之后，就会由 TIDYING -> TERMINATED
 * 线程池运行流程:
 *  1.添加任务，如果线程池中线程数没达到coreSize(核心线程数)，直接创建新线程执行
 *  2.达到coreSize，放入queue(队列)中
 *  3.queue已满，未达到maxSize(最大线程数),则继续创建线程
 *  4.达到maxSize，根据reject策略处理
 *  5.超时后，线程被释放，只保留核心线程
 *
 * @author HF
 * @date 2022-10-18
 **/
@SuppressWarnings({"all"})
public class ThreadPoolDemo {

    public static void main(String[] args) {
        // RUNNING 创建线程池
        ExecutorService poolExecutor = ThreadPoolUtil.getJdkThreadPool();
        // SHUTDOWN 关闭线程池
//        shutdownTest(poolExecutor);
        // STOP 停止线程池
//        shutdownNowTest(poolExecutor);
        // TIDYING 线程池调用shutdown()或者shutdownNow()方法后,线程池中所有任务执行完成时的状态
        // TERMINATED 线程池状态变为TIDYING后,会调用terminated()方法结束线程池,可以通过继承ThreadPoolExecutor来重写terminated()方法,实现自定义结束逻辑,参考: org.hf.application.javabase.thread.MyExecutorService
        // 查看线程池中线程状态
        threadState(poolExecutor);
    }

    /**
     * 查看线程池中线程状态: RUNNABLE、TIMED_WAITING、WAITING, debug查询线程状态
     * //debug add watcher: ((ThreadPoolExecutor) poolExecutor).workers.iterator().next().thread.getState()
     * @param poolExecutor 线程池对象
     */
    @SneakyThrows
    private static void threadState(ExecutorService poolExecutor) {
        poolExecutor.execute(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        // 主线程未阻塞前是 RUNNABLE 状态
        // 场景一: 这里阻塞后,线程池中线程是TIMED_WAITING状态
//        Thread.sleep(4000);
        // 场景二: 这里阻塞后,线程池中线程是WAITING状态
        Thread.sleep(6000);
        // 关闭线程池
        ThreadPoolUtil.closeJdkThreadPool(poolExecutor);
        System.out.println("ok");
    }

    /**
     * 停止接收新任务,停止已经提交的任务(包括正在执行(尝试中断,这里调用的是Thread.interrupt())和队列中等待(忽略)的任务),返回未执行的任务列表
     * @param poolExecutor 入参
     */
    private static void shutdownNowTest(ExecutorService poolExecutor) {
        // 尝试中断此任务,执行Thread.interrupt()
        poolExecutor.execute(() -> {
            try {
                // 这里中断sleep的线程(阻塞状态的线程)会抛出InterruptedException的异常
                Thread.sleep(1000);
                System.out.println("finish task 1");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        // 返回这个未执行的任务
        poolExecutor.execute(() -> {
            try {
                Thread.sleep(1000);
                System.out.println("finish task 2");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        // 改为shutdownNow后，任务立马终止，sleep被打断，新任务无法提交
        List<Runnable> runnableList = poolExecutor.shutdownNow();
        System.out.println("未执行任务列表" + runnableList.toString());
        // 拒绝这个任务添加
        poolExecutor.execute(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println("ok");
    }

    /**
     * 测试线程池SHUTDOWN 停止接收新任务,执行完已提交的任务(包括正在执行的和队列中等待的任务)
     * @param poolExecutor 入参
     */
    private static void shutdownTest(ExecutorService poolExecutor) {
        // 执行此任务
        poolExecutor.execute(() -> {
            try {
                Thread.sleep(1000);
                System.out.println("finish task 1");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        // shutdown后不接受新任务，已存在任务仍然可以执行完成
        poolExecutor.shutdown();
        // 拒绝此任务
        poolExecutor.execute(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println("ok");
    }

}

/**
 * <p> 自定义类，重写terminated方法 </p >
 * @author HF
 * @date 2022-10-19
 **/
class MyExecutorService extends ThreadPoolExecutor {

    public MyExecutorService(int corePoolSize, int maximumPoolSize, long keepAliveTime,
                             TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    @Override
    protected void terminated() {
        super.terminated();
        // 自定义结束时的行为
        System.out.println("terminated 自定义行为");
    }

    /**
     * 测试自定义线程池调用shutdownNow()或者shutdown()方法触发自定义结束行为
     */
    public static void main(String[] args) throws InterruptedException {
        // 自定义线程池创建
        MyExecutorService service = new MyExecutorService(1, 2, 10000, TimeUnit.SECONDS, new
                LinkedBlockingQueue<>(5));
        service.shutdownNow();
//        service.shutdown();
    }
}

/**
 * 创建线程池, 指定各线程参数
 * 手动创建jdk线程池; 参数: 1.核心线程数,2.最大线程数,3.线程存活时间,4.时间单位,5.提交任务队列,6.线程工厂:格式化线程名称,
 *      7.拒绝策略:超出提交任务队列的最大值则执行策略,这里的策略是直接丢弃不抛出异常
 */
class PoolFactoryDemo {

    public static void main(String[] args) throws InterruptedException {
        // 获取线程池
        ThreadPoolExecutor executor = getThreadPoolExecutor();
        for (int i = 0; i < 10; i++) {
            executor.execute(() -> {
                try {
                    System.out.println(Thread.currentThread().getName());
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        // 关闭线程池
        executor.shutdown();
        System.out.println("end");
    }

    /**
     * 创建线程池
     * @return ThreadPoolExecutor
     */
    private static ThreadPoolExecutor getThreadPoolExecutor() {
        return new ThreadPoolExecutor(10, 10, 20,
                TimeUnit.SECONDS, new ArrayBlockingQueue<>(5), new ThreadFactory() {
            /*线程池变量*/
            final AtomicInteger poolNumber = new AtomicInteger(1);
            /*分组*/
            final ThreadGroup group = System.getSecurityManager() == null ? Thread.currentThread().getThreadGroup() : System.getSecurityManager().getThreadGroup();
            /*线程变量*/
            final AtomicInteger threadNumber = new AtomicInteger(1);
            /*线程名称*/
            final String namePrefix = "myPool-" + poolNumber.getAndIncrement() + "-thread-";
            @Override
            public Thread newThread(@NonNull Runnable runnable) {
                // 参数1:分组参数; 参数2:Runnable对象,线程执行的run()逻辑; 参数3:执行线程名称
                return new Thread(group, runnable,namePrefix + threadNumber.getAndIncrement());
            }
        }, new ThreadPoolExecutor.DiscardPolicy());
    }

}

/**
 * <p> 线程池参数优化 </p >
 * 1. newCachedThreadPool
 * @author HF
 * @date 2022-11-25
 **/
@SuppressWarnings({"all"})
class ExecutorOptimization {
    public static void main(String[] args) {
        // Executors工具类创建线程
        /**
         * 只要线程不够用，就一直开，不用就全部释放。线程数0‐max之间弹性伸缩;
         * 注意：任务并发太高且耗时较长时，造成cpu高消耗，同时要警惕OOM
         * 参数: 核心线程数=0, 最大线程数=Integer.MAX_VALUE, 超时时间=60s, 队列大小=1
         */
//        ExecutorService executorService = Executors.newCachedThreadPool();
        /**
         * 线程数一直保持指定数量，不增不减，永不超时,如果不够用，就沿着队列一直追加上去，排队等候;
         * 注意：并发太高时，容易造成长时间等待无响应，如果任务临时变量数据过多，容易OOM
         * 参数: core等于最大线程数, 超时时间为0, 队列采用无界链表
         */
//        ExecutorService executorService = Executors.newFixedThreadPool(10);
        /**
         * 只有一个线程在慢吞吞的干活，可以认为是newFixedThreadPool的特例, 适用于任务零散提交，不紧急的情况
         * 参数: core等于最大线程数=1, 超时时间为0, 队列采用无界链表
         */
//        ExecutorService executorService = Executors.newSingleThreadExecutor();
        /**
         * 用于任务调度，DelayedWorkQueue限制住了任务可被获取的时机(getTask方法)，也就实现了时间控制
         * 核心线程数=指定入参, 最大线程数=Integer.MAX_VALUE, 超时时间为0, 队列DelayedWorkQueue来进行任务的时间控制
         */
//        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);
        // 这里定义的线程池采用拒绝策略
        ThreadPoolExecutor executor = new ThreadPoolExecutor(3, 5, 20,
                TimeUnit.SECONDS, new ArrayBlockingQueue<>(2), new ThreadFactoryBuilder().setNamePrefix("custom-name").build(),
                (runnable, executorParam) -> System.out.println("reject"));
        // 这里用来测试无限循环添加task，debug看workers和workQueue数量增长规律, 等待一段时间后，查看workers数量是否回落到core
        for (;;) {
            executor.execute(() -> {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
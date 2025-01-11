package org.hf.boot.springboot.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 统计执行耗时工具类
 * @author HF
 */
@Slf4j
public class PerfTrackerUtil {

    /**
     * 开始时间
     */
    private final long startTime;
    /**
     * 方法类名称
     */
    private final String className;
    /**
     * 方法名称
     */
    private final String methodName;
    /**
     * 最大耗时时间, 单位:毫秒
     */
    private final long maxTimeConsumed;

    private PerfTrackerUtil(String methodName, String className) {
        this.startTime = System.currentTimeMillis();
        this.methodName = methodName;
        this.className = className;
        this.maxTimeConsumed = 500;
    }

    private PerfTrackerUtil(String methodName, String className, long maxTimeConsumed) {
        this.startTime = System.currentTimeMillis();
        this.methodName = methodName;
        this.className = className;
        this.maxTimeConsumed = maxTimeConsumed;
    }

    public static TimerContext start() {
        return new TimerContext(Thread.currentThread().getStackTrace()[2].getMethodName(),
                Thread.currentThread().getStackTrace()[2].getClassName());
    }

    public static TimerContext start(long maxTimeConsumed) {
        return new TimerContext(Thread.currentThread().getStackTrace()[2].getMethodName(),
                Thread.currentThread().getStackTrace()[2].getClassName(), maxTimeConsumed);
    }

    /**
     * AutoCloseable自动关闭接口(因为方法执行完后会自动释放资源,由JVM执行,需要关闭的资源要定义在try()中)是jdk1.7之后的新特性
     * 此对象必须在try()中使用
     */
    public static class TimerContext implements AutoCloseable {
        private final PerfTrackerUtil tracker;

        private TimerContext(String methodName, String className) {
            this.tracker = new PerfTrackerUtil(methodName, className);
        }

        private TimerContext(String methodName, String className, long maxTimeConsumed) {
            this.tracker = new PerfTrackerUtil(methodName, className, maxTimeConsumed);
        }

        /**
         * 重写close()方法, 将需要在try-catch-finally中finally里面执行的逻辑执行在close()方法中实现即可
         */
        @Override
        public void close() {
            long executeTime = System.currentTimeMillis() - tracker.startTime;
            if (executeTime > tracker.maxTimeConsumed) {
                log.warn("慢查询告警, 类名={}, 方法={}, 耗时={}ms", tracker.className, tracker.methodName, executeTime);
            }
            log.info("耗时={}", executeTime);
        }
    }

    public static void main(String[] args) {
        // 统计耗时
        try (PerfTrackerUtil.TimerContext ignored = PerfTrackerUtil.start(3000)) {
            TimeUnit.SECONDS.sleep(2);
            System.out.println("耗时统计实例测试");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
package org.hf.boot.springboot.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * <p> 线程池 </p >
 * 实现AsyncConfigurer接口并重写getAsyncExecutor()方法, 修改spring的默认线程池为自定义线程池,
 * 实测不实现AsyncConfigurer接口, 打上@Async注解的方法也是通过自定义的线程池执行的, 这是因为这个
 * 类标注为了配置类并加上了@EnableAsync注解, 所以默认使用自定义线程池, 如果@EnableAsync添加在启
 * 动类上, 则是用springboot的默认线程池
 * @author hufei
 * @date 2022-09-22
 **/
@Slf4j
@Configuration
@EnableAsync
public class AsyncExecutorConfig implements AsyncConfigurer {
    /**
     * 核心线程数（ 默认线程数）
     */
    private static final int CORE_POOL_SIZE  = 5;
    /**
     * 最大线程数
     */
    private static final int MAX_POOL_SIZE = 20;
    /**
     * 允许线程空闲时间（单位：默认为秒）
     */
    private static final int KEEP_ALIVE_TIME = 100;
    /**
     * 缓冲队列数
     */
    private static final int QUEUE_CAPACITY = 10000;

    /**
     * bean的名称，默认为首字母小写的方法名
     */
    @Bean("customTaskExecutor")
    public ThreadPoolTaskExecutor customTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setKeepAliveSeconds(KEEP_ALIVE_TIME);
        executor.setThreadNamePrefix("customTaskExecutor-");
        // 线程池对拒绝任务的处理策略:丢弃最新的
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        /**
         * 在多线程中实现公共参数共享 - 2
         */
        // 设置线程池装饰器
        executor.setTaskDecorator(new CustomThreadPoolTaskDecorator());
        // 初始化
        executor.initialize();
        return executor;
    }

    /**
     * 修改springboot默认线程池
     */
    @Override
    public Executor getAsyncExecutor(){
        return customTaskExecutor();
    }

    /**
     * 异步方法执行的过程中抛出的异常捕获
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (throwable, method, objects) -> {
            String msg = StringUtils.EMPTY;
            if (ArrayUtils.isNotEmpty(objects) && objects.length > 0) {
                msg = StringUtils.join(msg, "参数是：");
                for (Object object : objects) {
                    msg = StringUtils.join(msg, object, ";");
                }
            }
            msg = StringUtils.join(msg, getThrowToString(throwable));
            log.error("线程池任务异常, 异常点={}#{}, 错误信息={}", method.getDeclaringClass(), method.getName(), msg);
        };
    }

    /**
     * 将异常堆栈信息转换成字符串
     *
     * @param throwable 异常
     * @return String
     */
    public static String getThrowToString(Throwable throwable) {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        throwable.printStackTrace(printWriter);
        return result.toString();
    }

    /**
     * 可调度执行任务的自定义线程池
     */
    @Bean("customScheduledExecutor")
    public ScheduledThreadPoolExecutor customScheduledExecutor() {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2,
                new CustomizableThreadFactory("customScheduledExecutor-"));
        // 线程池对拒绝任务的处理策略:丢弃最新的
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        return executor;
    }

}

package org.hf.boot.springboot.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 通过springboot的Scheduled实现定时任务
 * 注意:此方式是用同一个线程去执行定时任务,如果有多个任务可能出现阻塞
 * 解决:通过配置自定义线程池(org.hf.boot.springboot.config.AsyncExecutorConfig),然后将定时任务开启
 *      异步执行(@Async("customTaskExecutor"))即可解决任务阻塞问题
 */
@Slf4j
@Component
@EnableScheduling
public class ScheduleTaskDemo {

    @Scheduled(cron = "0/5 * * * * ? ")
    public void testTask1() throws InterruptedException {
        // 模拟任务阻塞, 这里由于在org.hf.boot.springboot.config.AsyncExecutorConfig中指定了默认线程池,所以没有出现任务阻塞
        Thread.sleep(15000);
        log.info("每5秒执行第1个任务,当前线程号={}, 线程名称={}", Thread.currentThread().getId(), Thread.currentThread().getName());
    }

    @Scheduled(cron = "0/10 * * * * ? ")
    public void testTask2() {
        log.info("每10秒执行第2个任务,当前线程号={}, 线程名称={}", Thread.currentThread().getId(), Thread.currentThread().getName());
    }

    @Async("customTaskExecutor")
    @Scheduled(cron = "0 0 9 * * ? ")
    public void testTask3() {
        log.info("每天上午九点执行第3个任务,当前线程号={}, 线程名称={}", Thread.currentThread().getId(), Thread.currentThread().getName());
    }

}
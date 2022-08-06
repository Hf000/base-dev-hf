package org.hf.boot.springboot.task.dynamicTask;

import com.dangdang.ddframe.job.executor.ShardingContexts;
import com.dangdang.ddframe.job.lite.api.listener.AbstractDistributeOnceElasticJobListener;

/**
 * @Author:hufei
 * @CreateTime:2020-12-01
 * @Description:创建监听器
 */
public class ElasticJobListener extends AbstractDistributeOnceElasticJobListener {

    /****
     * 构造函数
     * @param startedTimeoutMilliseconds
     * @param completedTimeoutMilliseconds
     */
    public ElasticJobListener(long startedTimeoutMilliseconds, long completedTimeoutMilliseconds) {
        super(startedTimeoutMilliseconds, completedTimeoutMilliseconds);
    }

    /***
     * 任务初始化前要做的事情，类似前置通知
     * @param shardingContexts
     */
    @Override
    public void doBeforeJobExecutedAtLastStarted(ShardingContexts shardingContexts) {
//        System.out.println("========doBeforeJobExecutedAtLastStarted========"+ TimeUtil.date2FormatHHmmss(new Date()));
    }

    /***
     * 任务执行完成后要做的事情，类似后置通知
     * @param shardingContexts
     */
    @Override
    public void doAfterJobExecutedAtLastCompleted(ShardingContexts shardingContexts) {
//        System.out.println("=======doAfterJobExecutedAtLastCompleted============="+ TimeUtil.date2FormatHHmmss(new Date()));
    }
}

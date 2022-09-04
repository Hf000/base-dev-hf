package org.hf.springboot.service.config;

import com.dangdang.ddframe.job.executor.ShardingContexts;
import com.dangdang.ddframe.job.lite.api.listener.AbstractDistributeOnceElasticJobListener;
import org.springframework.stereotype.Component;

/**
 * <p> 创建elastic-job定时任务监听器 </p>
 *
 * @author hufei
 * @date 2022/9/4 14:22
 */
@Component
public class ElasticJobListener extends AbstractDistributeOnceElasticJobListener {

    /****
     * 构造函数
     */
    public ElasticJobListener() {
        //初始化要给定超时多少秒重连
        super(100L, 100L);
    }

    /***
     * 任务初始化前要做的事情，类似前置通知
     * @param shardingContexts 定时任务上下文信息
     */
    @Override
    public void doBeforeJobExecutedAtLastStarted(ShardingContexts shardingContexts) {
//        System.out.println("========doBeforeJobExecutedAtLastStarted========"+ TimeUtil.date2FormatHHmmss(new Date()));
    }

    /***
     * 任务执行完成后要做的事情，类似后置通知
     * @param shardingContexts 定时任务上下文信息
     */
    @Override
    public void doAfterJobExecutedAtLastCompleted(ShardingContexts shardingContexts) {
//        System.out.println("=======doAfterJobExecutedAtLastCompleted============="+ TimeUtil.date2FormatHHmmss(new Date()));
    }
}

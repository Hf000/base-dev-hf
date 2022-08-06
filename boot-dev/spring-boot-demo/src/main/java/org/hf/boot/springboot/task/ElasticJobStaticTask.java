package org.hf.boot.springboot.task;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.elasticjob.lite.annotation.ElasticSimpleJob;
import org.springframework.stereotype.Component;

/**
 * @Author:hufei
 * @CreateTime:2020-12-01
 * @Description:elastic-job静态定时任务
 */
//cron:定时表达式；jobName：这里和application.yml中的namespace保持一致；shardingTotalCount：分片数量
@ElasticSimpleJob(cron = "5/10 * * * * ?", jobName = "myJob", shardingTotalCount = 1)
@Component
public class ElasticJobStaticTask implements SimpleJob {

    /**执行任务方法**/
    @Override
    public void execute(ShardingContext shardingContext) {
        System.out.println("-----------执行！");
    }
}

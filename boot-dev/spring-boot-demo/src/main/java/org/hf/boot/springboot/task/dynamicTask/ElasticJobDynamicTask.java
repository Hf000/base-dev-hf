package org.hf.boot.springboot.task.dynamicTask;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;

/**
 * @Author:hufei
 * @CreateTime:2020-12-01
 * @Description:任务执行
 */
public class ElasticJobDynamicTask implements SimpleJob {

    @Override
    public void execute(ShardingContext shardingContext) {
        //传递的参数
        String id = shardingContext.getJobParameter();
        try {
            //具体任务逻辑
            System.out.println("执行你的逻辑代码！param:"+id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

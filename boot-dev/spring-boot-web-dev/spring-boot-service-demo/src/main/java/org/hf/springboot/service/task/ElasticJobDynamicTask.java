package org.hf.springboot.service.task;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import lombok.extern.slf4j.Slf4j;

/**
 * <p> 构建动态定时任务 </p>
 * @author hufei
 * @date 2022/9/4 14:26
*/
@Slf4j
public class ElasticJobDynamicTask implements SimpleJob {

    @Override
    public void execute(ShardingContext shardingContext) {
        //传递的参数
        String param = shardingContext.getJobParameter();
        try {
            //具体任务逻辑
            log.info("执行你的逻辑代码！param:{}", param);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package org.hf.springboot.service.task;

import cn.hutool.core.date.DateUtil;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import lombok.extern.slf4j.Slf4j;
import org.hf.common.config.elasticjob.ElasticSimpleJob;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * <p> 定时任务实现 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2021/10/9 22:49
 */
@Slf4j
@Component
@ElasticSimpleJob(cron = "${ej.cron}")
public class ElasticJobTask implements SimpleJob {
    @Override
    public void execute(ShardingContext shardingContext) {
        log.info("定时任务测试===>{}", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
    }
}

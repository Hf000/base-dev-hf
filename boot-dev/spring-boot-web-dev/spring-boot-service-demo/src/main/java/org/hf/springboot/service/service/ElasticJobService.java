package org.hf.springboot.service.service;

import org.hf.springboot.service.pojo.bo.ElasticJobBO;

import java.util.List;

/**
 * <p> 定时任务支持接口 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2021/10/10 9:15
 */
public interface ElasticJobService {

    /**
     * 查询定时任务列表
     * @return 返回定时任务列表
     */
    List<ElasticJobBO> findElasticJobList();

    /**
     * 执行定时任务
     * @param jobName 任务名称
     */
    void executeTask(String jobName);

    /**
     * 重新执行定时任务
     * @param jobName 任务名称
     */
    void rescheduleJob(String jobName, String cron);
}

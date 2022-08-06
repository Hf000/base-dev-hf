package org.hf.springboot.web.pojo.vo;

import lombok.Data;

/**
 * <p> 定时任务响应实体 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2021/10/10 10:21
 */
@Data
public class ElasticJobVO {

    /**
     * 定时任务名称
     */
    private String jobName;

    /**
     * cron表达式
     */
    private String cron;

}
